add("data")
add("domain")
add("presentation")
add("remote-impl")
add("thread-impl")

addCommon("common-database")
addCommon("common-remote")
addCommon("common-thread")

addDatabaseImpl("database-impl-annotations")
addDatabaseImpl("database-impl-core")
addDatabaseImpl("database-impl-processor")

fun add(module: String) = include(":$module")

fun addCommon(module: String) = add("common:$module")

fun addDatabaseImpl(module: String) = add("database-impl:$module")