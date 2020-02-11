package org.studio.network.model

import com.google.gson.annotations.SerializedName

data class SpeciesNM (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("classification") val classification : String,
    @SerializedName("eye_colors") val eyeColors : String,
    @SerializedName("hair_colors") val hairColors : String,
    @SerializedName("people") val people : List<String>,
    @SerializedName("films") val films : List<String>,
    @SerializedName("url") val url : String
)