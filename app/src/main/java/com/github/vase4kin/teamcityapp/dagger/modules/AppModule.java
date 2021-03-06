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

package com.github.vase4kin.teamcityapp.dagger.modules;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.keychain.KeyChain;
import com.github.vase4kin.teamcityapp.BuildConfig;
import com.github.vase4kin.teamcityapp.TeamCityApplication;
import com.github.vase4kin.teamcityapp.api.GuestUserAuthInterceptor;
import com.github.vase4kin.teamcityapp.api.TeamCityAuthenticator;
import com.github.vase4kin.teamcityapp.api.cache.CacheProviders;
import com.github.vase4kin.teamcityapp.crypto.CryptoManager;
import com.github.vase4kin.teamcityapp.crypto.CryptoManagerImpl;
import com.github.vase4kin.teamcityapp.onboarding.OnboardingManager;
import com.github.vase4kin.teamcityapp.onboarding.OnboardingManagerImpl;
import com.github.vase4kin.teamcityapp.storage.SharedUserStorage;
import com.github.vase4kin.teamcityapp.storage.api.UserAccount;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class AppModule {

    private static final int CONNECTION_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 10;

    public static final String CLIENT_BASE = "base";
    public static final String CLIENT_AUTH = "auth";

    private TeamCityApplication mApplication;

    public AppModule(TeamCityApplication application) {
        mApplication = application;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected Context provideContext() {
        return mApplication.getApplicationContext();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected SharedUserStorage provideSharedUserStorage(CryptoManager cryptoManager) {
        return SharedUserStorage.init(mApplication.getApplicationContext(), cryptoManager);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Named(CLIENT_AUTH)
    @Provides
    protected OkHttpClient providesAuthHttpClient(SharedUserStorage sharedUserStorage,
                                                  @Named(CLIENT_BASE) OkHttpClient okHttpClient) {
        UserAccount userAccount = sharedUserStorage.getActiveUser();
        if (userAccount.isGuestUser()) {
            return okHttpClient.newBuilder()
                    .addInterceptor(new GuestUserAuthInterceptor())
                    .build();
        } else {
            return okHttpClient.newBuilder()
                    .authenticator(new TeamCityAuthenticator(userAccount))
                    .build();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Named(CLIENT_BASE)
    @Singleton
    @Provides
    protected OkHttpClient providesBaseHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        // TODO: Use DI separated modules for debug and release which will be holding this interceptor
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        return clientBuilder.build();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected CryptoManager providesCryptoManager() {
        KeyChain keyChain = new SharedPrefsBackedKeyChain(mApplication.getApplicationContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        return new CryptoManagerImpl(crypto);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected CacheProviders provideCacheProviders(RxCache rxCache) {
        return rxCache.using(CacheProviders.class);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected RxCache providesRxCache() {
        File cacheDir = mApplication.getFilesDir();
        return new RxCache.Builder()
                .persistence(cacheDir, new GsonSpeaker());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected FirebaseAnalytics providesFirebaseAnalytics() {
        return FirebaseAnalytics.getInstance(mApplication.getApplicationContext());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    @Provides
    @Singleton
    protected OnboardingManager providesOnboardingManager() {
        return new OnboardingManagerImpl(mApplication.getApplicationContext());
    }
}
