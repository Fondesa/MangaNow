package com.fondesa.manganow.splash.impl

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.ui.api.BaseActivity
import com.fondesa.manganow.ui.api.FullScreenActivityViewDelegate
import com.fondesa.manganow.ui.api.FullScreenActivityViewDelegateImpl
import com.fondesa.manganow.ui.api.util.addObservers
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

/**
 * Section that shows the launch screen.
 * This section will be dismissed automatically after a certain amount of time.
 */
class SplashActivity : BaseActivity<FullScreenActivityViewDelegate>(), SplashContract.View {

    @Inject
    internal lateinit var presenter: SplashContract.Presenter

    @Inject
    internal lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    override fun createViewManager(): FullScreenActivityViewDelegate =
        FullScreenActivityViewDelegateImpl(
            activity = this,
            contentLayout = R.layout.activity_splash,
            fitsSystemWindows = false
        )

    override fun onViewCreated(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        // Add all the lifecycle observers
        lifecycle.addObservers(lifecycleObservers)
        // Attach the view to the presenter.
        presenter.attach()

        retryButton.setOnClickListener {
            presenter.retryButtonClicked()
        }
    }

    override fun showProgressIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun showRetryButton() {
        retryButton.visibility = View.VISIBLE
    }

    override fun hideRetryButton() {
        retryButton.visibility = View.INVISIBLE
    }

    override fun showErrorForCause(cause: ErrorCause) {
        errorTextView.setText(cause.toErrorMessage())
        errorTextView.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        errorTextView.visibility = View.INVISIBLE
    }
}