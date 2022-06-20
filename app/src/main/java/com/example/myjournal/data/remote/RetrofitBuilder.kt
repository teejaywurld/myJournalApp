package com.example.myjournal.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
   private val baseUrl = "https://gossip-central-dev.herokuapp.com/api/v1/"


    fun getInstance(): Retrofit{
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //this above creates the instance of retrofit class library
    }

    val gossipCentralAPI : GossipCentralAPI = getInstance()
        .create(GossipCentralAPI::class.java)
    //This is the implementation of the GossipCentralAPI interface. i.e the interface in the remote
}