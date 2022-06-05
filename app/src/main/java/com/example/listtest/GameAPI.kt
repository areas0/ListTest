package com.example.listtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GameAPI {
    @GET("list.json")
    fun listGames() : Call<List<GameObject>>

    @GET("details{id}.json")
    fun getDetails(@Path("id") id : Int) : Call<GameDetails>
}