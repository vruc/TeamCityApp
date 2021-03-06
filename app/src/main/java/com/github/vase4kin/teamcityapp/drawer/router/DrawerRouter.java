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

package com.github.vase4kin.teamcityapp.drawer.router;

import com.github.vase4kin.teamcityapp.about.AboutLibrariesActivity;

/**
 * Drawer router
 */
public interface DrawerRouter {

    /**
     * Start {@link com.github.vase4kin.teamcityapp.root.view.RootProjectsActivity}
     */
    void startRootProjectsActivity();

    /**
     * Start {@link com.github.vase4kin.teamcityapp.root.view.RootProjectsActivity} when accounts are switched
     */
    void startRootProjectsActivityWhenSwitchingAccounts();

    /**
     * Start {@link com.github.vase4kin.teamcityapp.account.manage.view.AccountListActivity}
     */
    void startAccountListActivity();

    /**
     * Start {@link com.github.vase4kin.teamcityapp.agenttabs.view.AgentTabsActivity}
     */
    void startAgentActivity();

    /**
     * Start {@link com.github.vase4kin.teamcityapp.runningbuilds.view.RunningBuildsListActivity}
     */
    void startBuildRunningActivity();

    /**
     * Start {@link com.github.vase4kin.teamcityapp.queue.view.BuildQueueActivity}
     */
    void startQueuedBuildsActivity();

    /**
     * Start {@link AboutLibrariesActivity}
     */
    void startAboutActivity();
}
