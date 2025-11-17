package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.HandlerMethod
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent
import java.lang.reflect.Method


class HandlerMethodContainer {

    val handlers: MutableMap<String, MutableList<RequestMapping>> = hashMapOf()
    var matcherStrategy: RequestMappingsMatcherStrategy? = null

    fun registerController(bean: Any, method: Method, mappingInfo: List<RequestMappingInfo> ): HandlerMethod? {
        if (mappingInfo.isEmpty()) return null
        val handlerMethod = HandlerMethod(bean, method)
        val botHandlers = handlers.computeIfAbsent(mappingInfo[0].token) { ArrayList() }
        mappingInfo.forEach { info -> botHandlers.add(RequestMapping(info, handlerMethod)) }
        return handlerMethod
    }

    fun lookupHandlerMethod(telegramBotEvent: TelegramBotEvent): HandlerLookupResult {
        return HandlerLookupResult()
    }

    fun setMatcherStrategy(matcherStrategy: RequestMappingsMatcherStrategy) {
        this.matcherStrategy = matcherStrategy
    }
}

data class RequestMapping(
    private val mappingInfo: RequestMappingInfo? = null,
    private val handlerMethod: HandlerMethod? = null,
)

data class HandlerLookupResult(
    val handlerMethod: HandlerMethod? = null,
    val basePattern: String? = null,
    val templateVariables: Map<String, String>? = null,
)