package gr.atcom.gpslocationservice.retrofit

import gr.atcom.gpslocationservice.response.CommonResponse
import gr.atcom.gpslocationservice.response.DoorResponse
import gr.atcom.gpslocationservice.retrofit.NetworkClientFactory.getRetrofitInstance

class NetworkProviderImpl: NetworkProvider {

    private val gpsLocationApi: ApiInterface = getRetrofitInstance()

    override suspend fun openDoorAsync(): DoorResponse {
        return gpsLocationApi.openDoorAsync()
    }
}