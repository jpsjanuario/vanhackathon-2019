package br.com.mobile2you.m2ybase.ui.login

import br.com.mobile2you.m2ybase.data.remote.models.LoginRequest
import br.com.mobile2you.m2ybase.data.repositories.LoginRepository
import br.com.mobile2you.m2ybase.utils.extensions.singleSubscribe
import io.reactivex.disposables.CompositeDisposable

class LoginPresenter : LoginContract.Presenter {

    private var view: LoginContract.View? = null
    private var repository = LoginRepository
    private var disposable = CompositeDisposable()

    override fun onLoginClicked(userData: LoginRequest) {
        view?.displayLoading(true)
        tryToLogin(userData)
    }

    private fun tryToLogin(userData: LoginRequest) {
        repository.signIn(userData)
                .singleSubscribe(
                        onSuccess = {
                            view?.displayLoading(false)
                            view?.onLoginSucceeded(it)
                        },
                        onError = {
                            view?.displayLoading(false)
                            view?.displayError(it.message)
                        }
                )
    }

    override fun onRegisterClicked() {
        view?.openRegister()
    }

    override fun onForgotPasswordClicked() {
        view?.openForgotPassword()
    }

    override fun attachView(mvpView: LoginContract.View?) {
        this.view = mvpView
    }

    override fun detachView() {
        this.view = null
        disposable.dispose()
    }
}