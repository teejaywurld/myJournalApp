package com.example.myjournal.auth.events

import com.example.myjournal.data.model.LoginRequest
import com.example.myjournal.data.model.RegistrationRequest

sealed class AuthEvent{
    data class RegistrationEvent(val request: RegistrationRequest):AuthEvent()
    data class LoginEvent(val  request: LoginRequest): AuthEvent()
}
