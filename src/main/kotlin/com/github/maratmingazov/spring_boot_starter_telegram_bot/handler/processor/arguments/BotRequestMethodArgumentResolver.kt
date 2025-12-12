package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotPathVariable
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotRequestMethodArgumentResolver(): BotHandlerMethodArgumentResolver {

    companion object {
        private val logger = LoggerFactory.getLogger(BotRequestMethodArgumentResolver::class.java)
    }

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        if (methodParameter.hasParameterAnnotation(BotPathVariable::class.java)) {
            return false
        }
        val paramType = methodParameter.parameterType
        return TelegramBotRequest::class.java.isAssignableFrom(paramType) ||
                Chat::class.java.isAssignableFrom(paramType) ||
                User::class.java.isAssignableFrom(paramType) ||
                Message::class.java.isAssignableFrom(paramType) ||
                Update::class.java.isAssignableFrom(paramType) ||
                String::class.java.isAssignableFrom(paramType)


    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any? {
        val paramType = methodParameter.parameterType
        if (TelegramBotRequest::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest)
        } else if (Chat::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest.chat)
        } else if (User::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest.user)
        } else if (Message::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest.message)
        } else if (Update::class.java.isAssignableFrom(paramType)) {
            return validateValue(paramType, telegramBotRequest.update)
        } else if (String::class.java.isAssignableFrom(paramType)) {
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