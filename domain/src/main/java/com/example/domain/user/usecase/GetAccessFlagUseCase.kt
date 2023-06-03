package com.example.domain.user.usecase

import com.example.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccessFlagUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return userRepository.getAccessFlag()
    }
}