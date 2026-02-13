package com.despaircorp.domain.user.domain.repo

import com.despaircorp.domain.user.domain.model.User

interface UserRepository {

    suspend fun getUser(id: String): Result<User>
    suspend fun updateUserName(newName: String, id: String): Result<Unit>
    suspend fun updateUserImage(image: ByteArray, id: String): Result<Unit>
}

