package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.core.MethodParameter

interface BotHandlerMethodReturnValueHandler {

    fun supportsReturnType(returnType: MethodParameter): Boolean

    fun handleReturnValue(returnValue: Any, returnType: MethodParameter, telegramBotRequest: TelegramBotRequest): BaseRequest<*,*>
}