/*
 * Copyright (c) 2019 Fondesa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fondesa.manganow.database.api.client.execution

import android.database.Cursor
import com.fondesa.manganow.database.api.client.reader.DatabaseReader

/**
 * Type of [Executor] used to query the database with a [Cursor].
 * The [Cursor] must be passed from outside and will be handled by [QueryExecutor.Handler].
 *
 * @param cursor [Cursor] used to query the database.
 * @param reader initializer for the [DatabaseReader].
 * @param R type of [DatabaseReader].
 */
class QueryExecutor<R : DatabaseReader>(
    private val cursor: Cursor,
    private val reader: (Cursor) -> R
) :
    Executor<QueryExecutor.Handler<R>> {

    override fun execute(): Handler<R> =
        Handler(cursor, reader)

    /**
     * Class used to execute operations on the [Cursor].
     * This class is merely a layer used to read values from the database
     * in a simpler way.
     * The [Handler] can execute only one reading transaction at the same moment.
     * This means that the only possibility to execute multi-threading reading transactions
     * is to create more than one [Handler].
     *
     * @param cursor [Cursor] that must be handled.
     * @param reader initializer for the [DatabaseReader].
     * @param R type of [DatabaseReader].
     */
    class Handler<R : DatabaseReader> internal constructor(
        val cursor: Cursor,
        reader: (Cursor) -> R
    ) {

        /**
         * Reader used to read the records.
         * The reader will be lazily initialized to avoid the construction before its usage.
         */
        val reader by lazy { reader(cursor) }

        var isReading = false
            private set

        private val lock = Any()

        /**
         * Prepare the reading transaction and execute a block of code once.
         * This method could be very useful to optimize the reading on a large amount of records.
         * For example, this could be used to prepare the indexes before the reading.
         *
         * @param block block of code that must be executed once.
         * @return element mapped from others reading operations defined inside [block].
         */
        inline fun <T> prepare(crossinline block: Handler<R>.(R) -> T): T {
            var value: T? = null
            executeReading {
                value = block(this, reader)
            }
            return value!!
        }

        /**
         * Execute a block of code for each record returned from the query.
         * This can be useful to optimize the retrieving of the result instead of using [map].
         *
         * @param block block of code that must be execute for each record.
         */
        inline fun forEach(crossinline block: Handler<R>.(R) -> Unit) {
            executeReading {
                if (cursor.moveToFirst()) {
                    do {
                        block(this, reader)
                    } while (cursor.moveToNext())
                }
            }
        }

        /**
         * Transform each record returned from the query to a mapped element using a [DatabaseReader].
         * This can be useful to obtain all records in a list without looping on them manually.
         *
         * @param block block of code that must be execute for each record.
         */
        inline fun <reified T> map(crossinline block: Handler<R>.(R) -> T): List<T> {
            val list = mutableListOf<T>()
            executeReading {
                if (cursor.moveToFirst()) {
                    do {
                        val element = block(this, reader)
                        list.add(element)
                    } while (cursor.moveToNext())
                }
            }
            return list.toList()
        }

        /**
         * Get the first record obtained from the query's result.
         *
         * @param block block of code that must be executed to transform the record to a mapped element.
         * @return element mapped from the record or null if the record wasn't found.
         */
        inline fun <T> firstOrNull(crossinline block: Handler<R>.(R) -> T): T? {
            var value: T? = null
            executeReading {
                if (cursor.moveToFirst()) {
                    value = block(this, reader)
                }
            }
            return value
        }

        /**
         * Get the first record obtained from the query's result.
         *
         * @param block block of code that must be executed to transform the record to a mapped element.
         * @return element mapped from the record.
         * @throws NullPointerException if the record wasn't found.
         */
        inline fun <T> first(crossinline block: Handler<R>.(R) -> T): T =
            firstOrNull(block) ?: throw NullPointerException("The value wasn't found.")

        /**
         * Get the first column's value of the first row obtained from the query's result.
         * The value will be read as long.
         *
         * @return first value read as a long.
         * @throws NullPointerException if the value wasn't found.
         */
        fun simpleInteger(): Long = first { it.getLong(0) }

        /**
         * Get the first column's value of the first row obtained from the query's result.
         * The value will be read as string.
         *
         * @return first value read as a string.
         * @throws NullPointerException if the value wasn't found.
         */
        fun simpleText(): String = first { it.getString(0) }

        /**
         * Get the first column's value of the first row obtained from the query's result.
         * The value will be read as double.
         *
         * @return first value read asa  double.
         * @throws NullPointerException if the value wasn't found.
         */
        fun simpleReal(): Double = first { it.getDouble(0) }

        /**
         * Get the first column's value of the first row obtained from the query's result.
         * The value will be read as byte array.
         *
         * @return first value read as a byte array.
         * @throws NullPointerException if the value wasn't found.
         */
        fun simpleBlob(): ByteArray = first { it.getBlob(0) }

        /**
         * Start a reading transaction.
         */
        fun beginReading() {
            isReading = true
        }

        /**
         * End the reading transaction and close the [Cursor].
         */
        fun endReading() {
            isReading = false
            cursor.close()
        }

        /**
         * Execute a reading transaction of there isn't another transaction running.
         * If there's another transaction running, the block will be executed inside that one.
         *
         * @param block block of code that must be execute in a reading transaction.
         */
        fun executeReading(block: () -> Unit) = synchronized(lock) {
            var startedTransaction = false
            if (!isReading) {
                startedTransaction = true
                beginReading()
            }

            block()

            if (startedTransaction) {
                endReading()
            }
        }
    }
}