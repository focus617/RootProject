/*
 * Copyright 2022 WANG Yanke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.focus617.login.featureTest

import com.focus617.core.platform.event.AppLaunchedEvent
import com.focus617.core.platform.event.EventHandler
import timber.log.Timber
import javax.inject.Inject

class FeatureEventHandlers @Inject constructor() {
    val appLaunchedHandler = EventHandler<AppLaunchedEvent> {
        Timber.i("AppLaunchedEvent received by FeatureEventHandlers")
    }
}
