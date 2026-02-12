package com.despaircorp.domain.user.domain.repo

import com.despaircorp.domain.user.domain.model.User

interface UserRepository {

    suspend fun getUser(id: String): Result<User>
}