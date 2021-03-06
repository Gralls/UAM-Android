package com.springer.patryk.geo_photo.screens.authentication.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.springer.patryk.geo_photo.MainActivity;
import com.springer.patryk.geo_photo.R;
import com.springer.patryk.geo_photo.screens.authentication.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;


/**
 * Created by Patryk on 2017-03-11.
 */

public class RegisterFragment extends Fragment implements RegisterContract.View {

    @BindView(R.id.register_email)
    TextInputEditText mEmail;
    @BindView(R.id.register_email_layout)
    TextInputLayout mEmailLayout;
    @BindView(R.id.register_password)
    TextInputEditText mPassword;
    @BindView(R.id.register_password_layout)
    TextInputLayout mPasswordLayout;
    @BindView(R.id.confirm_password)
    TextInputEditText mConfirmPassword;
    @BindView(R.id.register_confirm_password_layout)
    TextInputLayout mConfirmPasswordLayout;
    @BindView(R.id.register_username)
    TextInputEditText mUsername;
    @BindView(R.id.register_username_layout)
    TextInputLayout mUsernameLayout;
    @BindView(R.id.register_button)
    AppCompatButton mSubmit;
    @BindView(R.id.signin_link)
    TextView loginLink;
    private RegisterContract.Presenter mPresenter;
    private RegisterFragmentCallback mCallback;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (AuthenticationActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(mCallback.toString() +
                    "must implement RegisterFragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RegisterPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.fragment_registration, null, false);
        ButterKnife.bind(this, registerView);


        Observable<Boolean> emailObservable = RxTextView.afterTextChangeEvents(mEmail)
                .skipInitialValue()
                .map(inputText -> inputText.editable().toString().matches("^\\S+@\\S+$"))
                .distinctUntilChanged();
        Observable<Boolean> confirmPasswordObservable = RxTextView.afterTextChangeEvents(mConfirmPassword)
                .skipInitialValue()
                .map(inputText -> inputText.editable().length() > 6)
                .distinctUntilChanged();
        Observable<Boolean> passwordObservable = RxTextView.afterTextChangeEvents(mPassword)
                .skipInitialValue()
                .map(inputText -> inputText.editable().length() > 6)
                .distinctUntilChanged();

        passwordObservable.subscribe(isValid -> mPasswordLayout.setError(isValid ? null : "Invalid password"));
        emailObservable.subscribe(isValid -> mEmailLayout.setError(isValid ? null : "Invalid email"));
        confirmPasswordObservable.subscribe(isValid -> mConfirmPasswordLayout.setError(isValid ? null : "Invalid password"));
        Observable.combineLatest(
                emailObservable,
                passwordObservable,
                confirmPasswordObservable,
                (emailValid, passwordValid, confirmPasswordValid) -> emailValid || passwordValid || confirmPasswordValid)
                .distinctUntilChanged()
                .subscribe(valid -> mSubmit.setEnabled(valid));

        mSubmit.setOnClickListener(view -> {
            mPresenter.checkCredentials(mUsername.getText().toString(), mEmail.getText().toString()
                    , mPassword.getText().toString(), mConfirmPassword.getText().toString()
            );
        });

        loginLink.setOnClickListener(view -> mCallback.showLoginFragment());

        return registerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAuthenticationError() {
        Toast.makeText(getContext(), "Authentication error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailError(int errorMessage) {
        mEmailLayout.setError(getString(errorMessage));
    }

    @Override
    public void showPasswordError(int errorMessage) {
        mPasswordLayout.setError(getString(errorMessage));
    }

    @Override
    public void showConfirmPasswordError(int errorMessage) {
        mConfirmPasswordLayout.setError(getString(errorMessage));
    }

    @Override
    public void showMainPage() {
        Toast.makeText(getContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public interface RegisterFragmentCallback {
        void showLoginFragment();
    }

}
