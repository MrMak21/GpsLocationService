package gr.atcom.gpslocationservice.model.door

import gr.atcom.gpslocationservice.model.common.Model
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DoorModel(
    val field: String = Model.INVALID_STRING
): Model