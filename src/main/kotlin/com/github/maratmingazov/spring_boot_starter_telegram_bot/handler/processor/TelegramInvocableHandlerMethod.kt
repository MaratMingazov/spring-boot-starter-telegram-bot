package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.pengrad.telegrambot.request.BaseRequest

class TelegramInvocableHandlerMethod(
    private val handlerMethod: HandlerMethod,
    private val argumentResolver: BotHandlerMethodArgumentResolver,
    private val returnValueHandler: BotHandlerMethodReturnValueHandler,
    ): HandlerMethod(handlerMethod) {

    fun invokeAndHandle(): BaseRequest<*, *>? {
        throw RuntimeException("")
    }
}