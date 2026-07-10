package com.guilda.protobuf.data.remote

import com.guilda.protobuf.data.model.CreateUserRequest
import com.guilda.protobuf.data.model.User
import com.guilda.protobuf.data.model.UserList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @Headers("Accept: application/x-protobuf")
    @GET("api/v1/users")
    suspend fun getUsers(): UserList

    @Headers("Accept: application/x-protobuf")
    @POST("api/v1/users")
    suspend fun createUser(@Body request: CreateUserRequest): User

    @Headers("Accept: application/x-protobuf")
    @DELETE("api/v1/users/{id}")
    suspend fun deleteUser(@Path("id") id: String): User
}
