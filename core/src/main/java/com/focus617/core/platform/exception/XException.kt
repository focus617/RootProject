package com.focus617.core.platform.exception

import kotlin.reflect.KClass

abstract class XException(message: String) : RuntimeException(message)

class XSystemCreationException(system: KClass<*>, details: String) :
    XException("Cannot create ${system.simpleName}. Did you add all necessary injectables?\nDetails: $details")

class XInjectableErrorException(injectable: KClass<*>) :
    XException("${injectable.simpleName} was injected error. Is it Injectable?}")
