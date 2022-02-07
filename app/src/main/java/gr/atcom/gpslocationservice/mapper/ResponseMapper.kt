package gr.atcom.gpslocationservice.mapper

import gr.atcom.gpslocationservice.model.common.nonNull
import gr.atcom.gpslocationservice.model.door.DoorModel
import gr.atcom.gpslocationservice.response.DoorResponse

fun DoorResponse.toModel(): DoorModel {
    return DoorModel(
        field = this.response.nonNull()
    )
}