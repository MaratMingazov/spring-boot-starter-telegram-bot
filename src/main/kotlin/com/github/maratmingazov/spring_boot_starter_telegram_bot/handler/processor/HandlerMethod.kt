package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import org.springframework.core.BridgeMethodResolver
import java.lang.reflect.Method

open class HandlerMethod(
    private val bean: Any,
    private val method: Method,
) {

//    val beanType: Class<*> = ClassUtils.getUserClass(bean)
    val bridgedMethod: Method = BridgeMethodResolver.findBridgedMethod(method)
//    val methodParameters: Array<MethodParameter> = initMethodParameters()

    constructor(handlerMethod: HandlerMethod) : this(handlerMethod.bean, handlerMethod.method) {
        // bridgedMethod и methodParameters будут инициализированы первичным конструктором,
        // но если важно скопировать *точно* такие же ссылки, можно явно прописать:
        // this.bridgedMethod = handlerMethod.bridgedMethod
        // this.methodParameters = handlerMethod.methodParameters
    }

    fun simpleName(): String {
        return "${bridgedMethod.declaringClass.typeName}.${bridgedMethod.name}"
    }
}