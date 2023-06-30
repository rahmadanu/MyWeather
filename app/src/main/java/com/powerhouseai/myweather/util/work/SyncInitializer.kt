/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powerhouseai.myweather.util.work

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer

object Sync {
    // This method is a workaround to manually initialize the sync process instead of relying on
    // automatic initialization with Androidx Startup. It is called from the app module's
    // Application.onCreate() and should be only done once.
    fun initialize(context: Context) {
        AppInitializer.getInstance(context)
            .initializeComponent(SyncInitializer::class.java)
    }
}

// This name should not be changed otherwise the app may have concurrent sync requests running
internal const val SyncWorkName = "SyncWorkName"

/**
 * Registers work to sync the data layer periodically on app startup.
 */
class SyncInitializer : Initializer<Sync> {
    override fun create(context: Context): Sync {
        enqueueWork(context)

        return Sync
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(WorkManagerInitializer::class.java)

    companion object {

        fun enqueueWork(context: Context) {
            WorkManager.getInstance(context).apply {
                // Run sync on app startup
                enqueueUniqueWork(
                    SyncWorkName,
                    ExistingWorkPolicy.REPLACE,
                    SyncWorker.startUpSyncWork(),
                )
            }
        }

        fun isWorkRunning(context: Context): LiveData<Boolean> =
            WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName)
                .map(MutableList<WorkInfo>::anyRunning)
    }
}

private val List<WorkInfo>.anyRunning get() = any { it.state == WorkInfo.State.RUNNING }
