package gr.atcom.gpslocationservice.retrofit

import gr.atcom.gpslocationservice.response.CommonResponse
import gr.atcom.gpslocationservice.response.DoorResponse

interface NetworkProvider {

    suspend fun openDoorAsync(): DoorResponse
}