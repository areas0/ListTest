package com.example.listtest

data class GameDetails(
    val id : Int,
    val name : String,
    val type : String,
    val players : Int,
    val year : Int,
    val picture : String,
    val url : String,
    val description_fr : String?,
    val description_en : String?,
)
