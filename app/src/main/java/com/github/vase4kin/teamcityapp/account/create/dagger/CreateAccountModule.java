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

package com.github.vase4kin.teamcityapp.account.create.dagger;

import android.app.Activity;
import android.content.Context;

import com.github.vase4kin.teamcityapp.account.create.data.CreateAccountDataManager;
import com.github.vase4kin.teamcityapp.account.create.data.CreateAccountDataManagerImpl;
import com.github.vase4kin.teamcityapp.account.create.data.CreateAccountDataModel;
import com.github.vase4kin.teamcityapp.account.create.data.CreateAccountDataModelImpl;
import com.github.vase4kin.teamcityapp.account.create.helper.UrlFormatter;
import com.github.vase4kin.teamcityapp.account.create.router.CreateAccountRouter;
import com.github.vase4kin.teamcityapp.account.create.router.CreateAccountRouterImpl;
import com.github.vase4kin.teamcityapp.account.create.tracker.CreateAccountTracker;
import com.github.vase4kin.teamcityapp.account.create.tracker.CreateAccountTrackerImpl;
import com.github.vase4kin.teamcityapp.account.create.tracker.FabricCreateAccountTrackerImpl;
import com.github.vase4kin.teamcityapp.account.create.tracker.FirebaseCreateAccountTrackerImpl;
import com.github.vase4kin.teamcityapp.account.create.view.CreateAccountView;
import com.github.vase4kin.teamcityapp.account.create.view.CreateAccountViewImpl;
import com.github.vase4kin.teamcityapp.storage.SharedUserStorage;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Set;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import okhttp3.OkHttpClient;

import static com.github.vase4kin.teamcityapp.dagger.modules.AppModule.CLIENT_BASE;

@Module
public class CreateAccountModule {

    private Activity mActivity;

    public CreateAccountModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    CreateAccountView providesCreateAccountView() {
        return new CreateAccountViewImpl(mActivity);
    }

    @Provides
    CreateAccountDataManager providesCreateAccountDataManager(Context context,
                                                              @Named(CLIENT_BASE) OkHttpClient okHttpClient,
                                                              SharedUserStorage sharedUserStorage,
                                                              UrlFormatter urlFormatter) {
        return new CreateAccountDataManagerImpl(context, okHttpClient, sharedUserStorage, urlFormatter);
    }

    @Provides
    CreateAccountDataModel providesCreateAccountDataModel(SharedUserStorage sharedUserStorage) {
        return new CreateAccountDataModelImpl(sharedUserStorage);
    }

    @Provides
    CreateAccountRouter providesCreateAccountRouter() {
        return new CreateAccountRouterImpl(mActivity);
    }

    @IntoSet
    @Provides
    CreateAccountTracker providesFabricCreateAccountTracker() {
        return new FabricCreateAccountTrackerImpl();
    }

    @IntoSet
    @Provides
    CreateAccountTracker providesFirebaseCreateAccountTracker(FirebaseAnalytics firebaseAnalytics) {
        return new FirebaseCreateAccountTrackerImpl(firebaseAnalytics);
    }

    @Provides
    CreateAccountTracker providesCreateAccountTracker(Set<CreateAccountTracker> trackers) {
        return new CreateAccountTrackerImpl(trackers);
    }

}
