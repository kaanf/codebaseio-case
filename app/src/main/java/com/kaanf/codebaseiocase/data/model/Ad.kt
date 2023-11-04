package com.kaanf.codebaseiocase.data.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class Ad(
    @SerializedName("bathCount")
    val bathCount: Int,
    @SerializedName("buildingAge")
    val buildingAge: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("curreny")
    val currency: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("gross")
    val gross: Int,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("label")
    val label: String?,
    @SerializedName("neighborhood")
    val neighborhood: String,
    @SerializedName("net")
    val net: Int,
    @SerializedName("price")
    val price: String,
    @SerializedName("room")
    val room: String,
    @SerializedName("roomCount")
    val roomCount: Int
)