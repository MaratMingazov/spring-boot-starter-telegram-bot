package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.springframework.core.MethodParameter

interface BotHandlerMethodArgumentResolver {

    fun supportsParameters(methodParameter: MethodParameter): Boolean

    fun resolveArgument(methodParameter: MethodParameter, telegramBotRequest: TelegramBotRequest): Any
}