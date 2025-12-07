package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.core.MethodParameter

interface BotHandlerMethodReturnValueHandler {

    fun supportsReturnType(returnType: MethodParameter): Boolean

    /**
     * Мы вызвали метод, который помечен аннотацией @BotRequest
     * Этот метод нам вернул значение [returnValue] и у него тир [MethodParameter]
     * Теперь нам нужно его обработать
     */
    fun handleReturnValue(returnValue: Any, returnType: MethodParameter, telegramBotRequest: TelegramBotRequest): BaseRequest<*,*>?
}