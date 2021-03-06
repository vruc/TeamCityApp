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

package com.github.vase4kin.teamcityapp.root.data;

import android.content.Context;
import android.webkit.CookieManager;

import com.github.vase4kin.teamcityapp.TeamCityApplication;
import com.github.vase4kin.teamcityapp.api.Repository;
import com.github.vase4kin.teamcityapp.drawer.data.DrawerDataManagerImpl;
import com.github.vase4kin.teamcityapp.storage.SharedUserStorage;
import com.github.vase4kin.teamcityapp.storage.api.UserAccount;

import io.rx_cache.internal.RxCache;

/**
 * Impl of {@link RootDataManager}
 */
public class RootDataManagerImpl extends DrawerDataManagerImpl implements RootDataManager {

    private final Context mContext;
    private final RxCache mRxCache;

    public RootDataManagerImpl(Context context,
                               Repository repository,
                               SharedUserStorage sharedUserStorage,
                               RxCache rxCache) {
        super(repository, sharedUserStorage);
        this.mContext = context;
        this.mRxCache = rxCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAccount getActiveUser() {
        return mSharedUserStorage.getActiveUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initTeamCityService() {
        ((TeamCityApplication) mContext.getApplicationContext()).buildRestApiInjectorWithBaseUrl(mSharedUserStorage.getActiveUser().getTeamcityUrl());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearAllWebViewCookies() {
        CookieManager.getInstance().removeAllCookie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evictAllCache() {
        mRxCache.evictAll().subscribe();
    }
}
