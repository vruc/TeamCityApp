/*
 * Copyright 2016 Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.vase4kin.teamcityapp.login.view;

/**
 * View callbacks of {@link LoginActivity}
 */
public interface LoginView {

    /**
     * Init views
     * @param listener
     */
    void initViews(OnLoginButtonClickListener listener);

    /**
     * Close activity
     */
    void close();

    /**
     * Show progress loading dialog
     */
    void showProgressDialog();

    /**
     * Dismiss progress loading dialog
     */
    void dismissProgressDialog();

    /**
     * Start animation
     */
    void animate();

    /**
     * Unbind views before activity is destroyed
     */
    void unbindViews();

    /**
     * Show auth error
     *
     * @param errorMessage - Error message which comes from server response
     */
    void showError(String errorMessage);

    /**
     * Hide error
     */
    void hideError();

    /**
     * Show server url cannot be empty error
     */
    void showServerUrlCanNotBeEmptyError();

    /**
     * Show server user name cannot be empty error
     */
    void showUserNameCanNotBeEmptyError();

    /**
     * Show server password cannot be empty error
     */
    void showPasswordCanNotBeEmptyError();

    /**
     * Show could not save user data error
     */
    void showCouldNotSaveUserError();

    /**
     * Handle activity callback
     */
    void onWindowFocusChanged(boolean hasFocus);

    /**
     * Hide keyboard
     */
    void hideKeyboard();

    /**
     * Show permissions unauthorized dialog
     */
    void showUnauthorizedInfoDialog();
}
