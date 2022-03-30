/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.focus617.core.platform.functional

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}

class BookFailure {
    class ListNotAvailable: Failure.FeatureFailure()
    class NonExistentBook: Failure.FeatureFailure()
}

//private fun handleFailure(failure: Failure?) {
//    when (failure) {
//        is NetworkConnection -> renderFailure(R.string.failure_network_connection)
//        is ServerError -> renderFailure(R.string.failure_server_error)
//        is ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
//    }
//}
//
//private fun renderFailure(@StringRes message: Int) {
//    movieList.invisible()
//    emptyView.visible()
//    hideProgress()
//    notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
//}
