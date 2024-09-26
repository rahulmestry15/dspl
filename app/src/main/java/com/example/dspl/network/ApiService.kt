package com.example.dspl.network

import com.example.dspl.ui.theme.model.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("todos")
    suspend fun getUserList(): Response<ArrayList<Task>>

}