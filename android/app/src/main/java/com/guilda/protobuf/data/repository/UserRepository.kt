package com.guilda.protobuf.data.repository

import com.guilda.protobuf.data.model.Role
import com.guilda.protobuf.data.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun createUser(name: String, email: String, roles: List<Role>): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}
