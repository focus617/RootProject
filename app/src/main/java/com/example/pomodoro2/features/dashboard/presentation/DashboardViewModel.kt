package com.example.pomodoro2.features.dashboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.features.infra.network.Api
import com.example.pomodoro2.features.infra.network.ApiConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * LiveData for this viewModel
     */
    // The internal MutableLiveData String that stores the most recent response
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    // The external immutable LiveData for the response String
    val text: LiveData<String> = _text

    // singleton Retrofit service instance
    val api = Api.create()
    public val imgSrcUrl:String = ApiConstants.imgSrcUrl

    // Network Status
    private val _status = MutableLiveData<Api.Companion.ApiStatus>()
    val status: LiveData<Api.Companion.ApiStatus>
        get() = _status



    /**
     * To initialize the projects variable as soon as possible
     */
    init {
        initializeDomainEntities()
    }

    private fun initializeDomainEntities() {
/*
        api.getProperties().enqueue(
            object : Callback<Contact> {
                override fun onFailure(call: Call<Contact>, t: Throwable) {
                    _text.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                    _text.value = "Success: " + response.body()
                }
            })
*/

        coroutineScope.launch {
            // creates and starts the network call on a background thread
            var getPropertiesDeferred = api.getPropertiesAsync()

            try {
                _status.value = Api.Companion.ApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = Api.Companion.ApiStatus.DONE
                _text.value = "Success: \n\n ${listResult.toString()} "
            } catch (e: Exception) {
                _status.value = Api.Companion.ApiStatus.ERROR
                _text.value = "Failure: ${e.message}"
            }
        }

    }


}