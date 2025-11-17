package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.HandlerMethod
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent
import java.lang.reflect.Method


class HandlerMethodContainer {

    /**
     * Содержит свзять telegramToken и список @BotRequest методов с информацией о методе
     */
    val handlers: MutableMap<String, MutableList<RequestMapping>> = hashMapOf()
    var matcherStrategy: RequestMappingsMatcherStrategy? = null
        set(value) {
            field = value
            // Мы еще хотим дополнительно отсортировать наши @BotRequest методы
            // Чтобы наверху оказались методы, у которых задан pattern
            // А последними будут проверяться методы универсальные, которые могут любое сообщение обработать
            handlers.replaceAll { _, value -> field.postProcess(value) }
        }

    fun registerController(bean: Any, method: Method, mappingInfo: List<RequestMappingInfo> ): HandlerMethod? {
        if (mappingInfo.isEmpty()) return null
        val handlerMethod = HandlerMethod(bean, method)
        val botHandlers = handlers.computeIfAbsent(mappingInfo[0].token) { ArrayList() }
        mappingInfo.forEach { info -> botHandlers.add(RequestMapping(info, handlerMethod)) }
        return handlerMethod
    }

    fun lookupHandlerMethod(telegramBotEvent: TelegramBotEvent): HandlerLookupResult {
        // Получаем список @BotRequest методов для текущего telegram token
        val botRequestMethods = handlers[telegramBotEvent.token]
        botRequestMethods?.let {
            // Перебираем все методы и проверяем может ли этот метод обработать наш Event
            it.forEach { botMapping ->
                val info = botMapping.mappingInfo
                if (info.token == telegramBotEvent.token && matcherStrategy!!.isMatched(telegramBotEvent, info)) {
                    val variables = matcherStrategy!!.extractPatternVariables(telegramBotEvent.text, info)
                    return HandlerLookupResult(botMapping.handlerMethod, info.pattern, variables)
                }
            }
        }
        return HandlerLookupResult()

    }
}

data class RequestMapping(
    val mappingInfo: RequestMappingInfo,
    val handlerMethod: HandlerMethod,
)

data class HandlerLookupResult(
    val handlerMethod: HandlerMethod? = null,
    val basePattern: String? = null,
    val templateVariables: Map<String, String>? = null,
)