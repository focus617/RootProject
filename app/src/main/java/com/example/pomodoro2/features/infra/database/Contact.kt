package com.example.pomodoro2.features.infra.database

data class Phone(
    val home: String,
    val mobile: String
)

data class Contact(
    val name: String,
    val email: String,
    val phone: Phone
)