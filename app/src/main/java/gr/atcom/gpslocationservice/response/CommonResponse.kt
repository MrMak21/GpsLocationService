package gr.atcom.gpslocationservice.response

import com.google.gson.annotations.SerializedName
import gr.atcom.gpslocationservice.model.common.Model

open class CommonResponse(
    @SerializedName("error") val error: String? = Model.INVALID_STRING,
    @SerializedName("error_description") val error_description: String? = Model.INVALID_STRING,
    @SerializedName("errors") val errors: List<Error>? = null,
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("resumeOptions") val resumeOptions: List<String>? = null,
    @SerializedName("processInstanceId") val processInstanceId: String? = null,
) {
    companion object {
        const val FIELD_NAME_ORIGINAL_UUID = "originalUuid"
    }
}