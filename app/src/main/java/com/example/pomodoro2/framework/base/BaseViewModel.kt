package com.example.pomodoro2.framework.base

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.*
import com.example.pomodoro2.data.DataSourceContainer
import com.example.pomodoro2.data.DefaultTaskRepository
import com.example.pomodoro2.framework.BaseApplication
import com.example.pomodoro2.platform.exception.Failure
import com.example.pomodoro2.platform.interactor.Interactors

/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel(application: Application) :
    AndroidViewModel(application) {

    protected val application: BaseApplication = getApplication()


    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    protected fun handleFailure(failure: Failure) {
        _failure.value = failure
    }
}

/**
 * Below class is partial refactor for factory build of viewmodel
 * Don't use it now
 */
open class MyBaseViewModel(application: Application) :
    BaseViewModel(application) {

    protected fun <T : BaseViewModel?> build(
        @NonNull owner: ViewModelStoreOwner,
        dataSourceContainer: DataSourceContainer,
        dependencies: Interactors,
        modelClass: Class<T>
    ): T {
        // Build the ViewModelFactory with Interactors for this feature
        val taskRepository = DefaultTaskRepository.getInstance(
            dataSourceContainer.roomTaskDataSource,
            dataSourceContainer.inMemoryDataSource
        )
        inject(
            this.application,
            dependencies
        )

        // Get a reference to the ViewModel associated with this fragment.
        return ViewModelProvider(owner,
            BaseViewModelFactory
        ).get(modelClass)
    }


    /**
     * This is pretty much boiler plate code for a ViewModel Factory.
     * Provides the context to the ViewModel.
     */
    companion object BaseViewModelFactory : ViewModelProvider.Factory {

        lateinit var application: Application
        lateinit var dependencies: Interactors

        fun inject(application: Application, dependencies: Interactors) {
            BaseViewModelFactory.application = application
            BaseViewModelFactory.dependencies = dependencies
        }

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (BaseViewModel::class.java.isAssignableFrom(modelClass)) {
                return modelClass.getConstructor(Application::class.java, Interactors::class.java)
                    .newInstance(
                        application,
                        dependencies
                    )
            } else {
                throw IllegalStateException("${modelClass.name} must extend BaseViewModel")
            }
        }
    }

}













