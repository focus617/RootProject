package com.focus617.mylib.logging

import mu.KotlinLogging.logger
import org.slf4j.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * The unwrapCompanionClass() method ensures that we do not generate a logger
 * named after the companion object but rather the enclosing class.
 * This is the current recommended way to find the class containing the
 * companion object.
 */
// Return logger for Java class, if companion object fix the name
fun <T: Any> logger(forClass: Class<T>): Logger {
    return logger(unwrapCompanionClass(forClass).simpleName)
}


// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}

// unwrap companion class to enclosing class given a Kotlin Class
fun <T: Any> unwrapCompanionClass(ofClass: KClass<T>): KClass<*> {
    return unwrapCompanionClass(ofClass.java).kotlin
}

// Return logger for Kotlin class
fun <T: Any> logger(forClass: KClass<T>): Logger {
    return logger(forClass.java)
}

// return logger from extended class (or the enclosing class)
fun <T: Any> T.logger(): Logger {
    return logger(this.javaClass)
}

// return a lazy logger property delegate for enclosing class
fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { logger(this.javaClass) }
}

// return a logger property delegate for enclosing class
fun <R : Any> R.injectLogger(): Lazy<Logger> {
    return lazyOf(logger(this.javaClass))
}


/**
 * abstract base class to provide logging,
 * intended for companion objects more than classes but works for either
 */
interface ILoggable {
    fun ILoggable.logger(): Logger = logger(this.javaClass)
}

abstract class WithLogging: ILoggable {
    val LOG = logger()
}
