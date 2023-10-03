package com.example.lab5_api_example

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("random_joke")
    fun getRandomJoke(): Call<Joke>
}