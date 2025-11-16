package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.RequestMappingInfo
import java.lang.reflect.Method


class HandlerMethodContainer {

    val handlers: MutableMap<String, MutableList<RequestMapping>> = hashMapOf()

    fun registerController(bean: Any, method: Method, mappingInfo: List<RequestMappingInfo> ): HandlerMethod? {
        if (mappingInfo.isEmpty()) return null
        val handlerMethod = HandlerMethod(bean, method)
        val botHandlers = handlers.computeIfAbsent(mappingInfo[0].token) { ArrayList() }
        mappingInfo.forEach { info -> botHandlers.add(RequestMapping(info, handlerMethod)) }
        return handlerMethod
    }
}

data class RequestMapping(
    private val mappingInfo: RequestMappingInfo? = null,
    private val handlerMethod: HandlerMethod? = null,
)