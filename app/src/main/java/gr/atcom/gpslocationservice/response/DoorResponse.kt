package gr.atcom.gpslocationservice.response

import com.google.gson.annotations.SerializedName

data class DoorResponse(
    @SerializedName("testResponse") val response: String
): CommonResponse()