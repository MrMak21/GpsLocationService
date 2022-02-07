package gr.atcom.gpslocationservice.repositories

import gr.atcom.gpslocationservice.mapper.toModel
import gr.atcom.gpslocationservice.model.common.DataResult
import gr.atcom.gpslocationservice.model.common.ErrorModel
import gr.atcom.gpslocationservice.model.door.DoorModel
import gr.atcom.gpslocationservice.retrofit.NetworkProvider
import timber.log.Timber

class MapsRepositoryImpl(private val networkProvider: NetworkProvider): MapsRepository {


    override suspend fun openDoorAsync(): DataResult<DoorModel, Throwable> {
        return try {
            val response = networkProvider.openDoorAsync()
            val model = response.toModel()
            DataResult(model)
        } catch (t: Throwable) {
            Timber.d(t.message)
            DataResult(error = t)
        }
    }
}