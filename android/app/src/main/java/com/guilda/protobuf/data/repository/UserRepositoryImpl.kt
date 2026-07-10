package com.guilda.protobuf.data.repository

import com.guilda.protobuf.data.model.Role
import com.guilda.protobuf.data.model.User
import com.guilda.protobuf.data.model.createUserRequest
import com.guilda.protobuf.data.remote.UserApi
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> = runCatching {
        api.getUsers().usersList
    }

    override suspend fun createUser(
        name: String,
        email: String,
        roles: List<Role>
    ): Result<User> = runCatching {
        val request = createUserRequest {
            this.name  = name
            this.email = email
            this.roles += roles
        }
        api.createUser(request)
    }

    override suspend fun deleteUser(id: String): Result<Unit> = runCatching {
        api.deleteUser(id)
        Unit
    }
}
