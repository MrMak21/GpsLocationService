package gr.atcom.gpslocationservice.repositories

import gr.atcom.gpslocationservice.model.common.DataResult
import gr.atcom.gpslocationservice.model.door.DoorModel

interface MapsRepository {

    suspend fun openDoorAsync(): DataResult<DoorModel, Throwable>
}