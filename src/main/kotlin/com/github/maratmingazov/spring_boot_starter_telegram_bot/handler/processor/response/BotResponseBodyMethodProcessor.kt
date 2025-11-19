package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.core.MethodParameter

class BotResponseBodyMethodProcessor(): BotHandlerMethodReturnValueHandler {

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        return false
    }

    override fun handleReturnValue(
        returnValue: Any,
        returnType: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): BaseRequest<*, *> {
        throw RuntimeException("")
    }
}