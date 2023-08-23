package com.example.test.model

import com.google.gson.annotations.SerializedName

data class DiningData(
    @SerializedName("seq") val seq: Int,
    @SerializedName("name") val name: String,
    @SerializedName("field") val field: String,
    @SerializedName("address") val address:String,

)
