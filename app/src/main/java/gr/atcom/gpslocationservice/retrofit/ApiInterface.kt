package gr.atcom.gpslocationservice.retrofit

import gr.atcom.gpslocationservice.response.CommonResponse
import gr.atcom.gpslocationservice.response.DoorResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET("/openDoor")
    suspend fun openDoorAsync(): DoorResponse
}