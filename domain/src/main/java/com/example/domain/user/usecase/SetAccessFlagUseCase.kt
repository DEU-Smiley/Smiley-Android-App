package com.example.domain.user.usecase

import com.example.domain.user.UserRepository
import javax.inject.Inject

class SetAccessFlagUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(flag: Boolean){
        userRepository.setAccessFlag(flag)
    }
}