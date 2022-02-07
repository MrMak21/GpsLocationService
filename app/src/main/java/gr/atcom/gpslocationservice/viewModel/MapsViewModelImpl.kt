package gr.atcom.gpslocationservice.viewModel

import android.app.Application
import androidx.annotation.UiThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import gr.atcom.gpslocationservice.application.GpsLocationApplication
import gr.atcom.gpslocationservice.model.common.DataResult
import gr.atcom.gpslocationservice.repositories.MapsRepositoryImpl
import kotlinx.coroutines.*
import timber.log.Timber

class MapsViewModelImpl(application: Application) : AndroidViewModel(application),
    MapsViewModel {

    private var uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    private var bgDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val bgDispatcherNonPausable: CoroutineDispatcher = Dispatchers.IO

    private val repo = MapsRepositoryImpl(GpsLocationApplication.get().networkProvider)

    override val notificationData: MutableLiveData<Pair<String, Int>> by lazy {
        MutableLiveData<Pair<String, Int>>()
    }

    override fun openDoorAsync(geofenceId: String, geofenceTransition: Int) {
        GlobalScope.launch {
            val response = executeNetworkCall {
                repo.openDoorAsync()
            }

            response.data?.let { doorModel ->
                Timber.d(doorModel.field)
                notificationData.postValue(Pair(geofenceId, geofenceTransition))
            } ?: response.error?.let {
                Timber.d(it)
                // Right now this code is running in catch block because the return from the server
                // is a simple String and not json value. I should make a change in the server side
                // but this is not Android issue.
                notificationData.postValue(Pair(geofenceId, geofenceTransition))
            }
        }
    }

    @UiThread
    private suspend fun <T, E : Throwable> executeNetworkCall(
        handleLoading: Boolean = false,
        isPausable: Boolean = true,
        networkCall: suspend () -> DataResult<T, E>
    ): DataResult<T, E> {


        val bgDispatcher = if (isPausable) bgDispatcher else bgDispatcherNonPausable
        return withContext(bgDispatcher) { networkCall() }
    }
}