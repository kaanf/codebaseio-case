package com.kaanf.codebaseiocase.data.model


import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class AdList(
    @SerializedName("data")
    val data: List<Ad>
)