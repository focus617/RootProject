package com.example.pomodoro2.platform.domain

//import lombok.Getter
import java.util.UUID


//@Getter
abstract class BaseDomainEvent {
    private val _id: String = UUID.randomUUID().toString()
    private val _createdAt = System.currentTimeMillis()
    override fun toString(): String {
        return this.javaClass.simpleName + "[" + _id + "]"
    }
}