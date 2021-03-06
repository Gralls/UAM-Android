package com.springer.patryk.geo_photo.screens.authentication.register;

import android.support.annotation.StringRes;

import com.springer.patryk.geo_photo.BasePresenter;
import com.springer.patryk.geo_photo.BaseView;

/**
 * Created by Patryk on 2017-03-11.
 */

interface RegisterContract {

    interface View extends BaseView<Presenter> {

        void showAuthenticationError();

        void showEmailError(@StringRes int errorMessage);

        void showPasswordError(@StringRes int errorMessage);

        void showConfirmPasswordError(@StringRes int errorMessage);

        void showMainPage();

    }

    interface Presenter extends BasePresenter {

        void checkCredentials(String username, String email, String password, String confirmPassword);

    }

}
