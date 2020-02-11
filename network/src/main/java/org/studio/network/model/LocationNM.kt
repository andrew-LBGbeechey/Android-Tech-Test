package org.studio.network.model

import com.google.gson.annotations.SerializedName

data class LocationNM (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("climate") val climate : String,
    @SerializedName("terrain") val terrain : String,
    @SerializedName("surface_water") val surfaceWater : Int,
    @SerializedName("residents") val residents : List<String>,
    @SerializedName("films") val films : List<String>,
    @SerializedName("url") val url : List<String>
)