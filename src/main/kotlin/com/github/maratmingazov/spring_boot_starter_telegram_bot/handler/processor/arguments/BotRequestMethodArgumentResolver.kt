package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotRequestMethodArgumentResolver(): BotHandlerMethodArgumentResolver {

    companion object {
        private val logger = LoggerFactory.getLogger(BotRequestMethodArgumentResolver::class.java)
    }

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        return false
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any? {
        val paramType = methodParameter.parameterType
        if (TelegramBotRequest::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest)
        }
        if (String::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest.text)
        }
        return null
    }

    private fun validateValue(paramType: Class<*>, value: Any?): Any? {
        if (value != null && !paramType.isInstance(value)) {
            logger.error("Current request is not of type [" + paramType.getName() + "]: " + value + "")
            return null
        }

        return value
    }
}