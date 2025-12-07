package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotPathVariable
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.springframework.core.MethodParameter

class BotRequestMethodPathArgumentResolver(): BotHandlerMethodArgumentResolver {

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        val paramType = methodParameter.parameterType
        return methodParameter.hasParameterAnnotation(BotPathVariable::class.java) &&
                String::class.java.isAssignableFrom(paramType) ||
                Int::class.java.isAssignableFrom(paramType)


    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any? {
        val paramType = methodParameter.parameterType
        val annotation = methodParameter.getParameterAnnotation(BotPathVariable::class.java)

        return null
    }
}