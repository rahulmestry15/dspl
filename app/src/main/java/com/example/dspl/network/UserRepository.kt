package com.example.dspl.network

// UserRepository.kt
class UserRepository {
    private val userService = RetrofitBase.api

    suspend fun getUsers() = userService.getUserList()

}
