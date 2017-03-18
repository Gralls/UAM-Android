package com.springer.patryk.uam_android.authentication.login;

import android.support.annotation.StringRes;

import com.springer.patryk.uam_android.BasePresenter;
import com.springer.patryk.uam_android.BaseView;

/**
 * Created by Patryk on 2017-03-11.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showAuthenticationError();

        void setEmailError(@StringRes int errorMessage);

        void setPasswordError(@StringRes int errorMessage);

        void showMainPage();

    }

    interface Presenter extends BasePresenter {

        void checkCredentials(String email, String password);

    }

}