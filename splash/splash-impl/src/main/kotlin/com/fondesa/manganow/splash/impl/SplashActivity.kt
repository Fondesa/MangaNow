package com.fondesa.manganow.splash.impl

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.ui.api.BaseActivity
import com.fondesa.manganow.ui.api.FullScreenViewManager
import com.fondesa.manganow.ui.api.util.addObservers
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_splash.*
import javax.inject.Inject

/**
 * Section that shows the launch screen.
 * This section will be dismissed automatically after a certain amount of time.
 */
class SplashActivity : BaseActivity<FullScreenViewManager>(), SplashContract.View {

    @Inject
    internal lateinit var presenter: SplashContract.Presenter

    @Inject
    internal lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    override fun createViewManager() = FullScreenViewManager(R.layout.activity_splash, false)

    override fun onViewCreated(view: ViewGroup, savedInstanceState: Bundle?) {
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

    override fun navigateToMainScreen() {
//        val intent = LatestActivity.createIntent(this)
//        // Don't retain this Activity in the stack.
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
    }
}