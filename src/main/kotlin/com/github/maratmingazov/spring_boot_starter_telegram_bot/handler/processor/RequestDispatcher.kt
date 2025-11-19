package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.HandlerLookupResult
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.HandlerMethodContainer
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler

class RequestDispatcher(
    private val handlerMethodContainer: HandlerMethodContainer,
    private val argumentResolver: BotHandlerMethodArgumentResolver,
    private val returnValueHandler: BotHandlerMethodReturnValueHandler,
) {

    /**
     * Должен найти контроллер (помеченный как @BotController) и у этого контроллера вызвать правильный метод (помеченный @BotRequest)
     */
    fun execute(telegramEvent: TelegramBotEvent) {
        // Нам нужно найти метод, в который мы передадим этот запрос
        val lookupResult = handlerMethodContainer.lookupHandlerMethod(telegramEvent)
        val handlerMethod = lookupResult.handlerMethod

        // теперь нам нужно этот метод вызвать
        val telegramBotRequest = TelegramBotRequest(
            telegramBot = telegramEvent.telegramBot,
            update = telegramEvent.update,
            messageType = telegramEvent.messageType,
            basePattern = lookupResult.basePattern,
            templateVariables = lookupResult.templateVariables,
            message = telegramEvent.message,
            text = telegramEvent.text,
            chat = telegramEvent.chat,
            user = telegramEvent.user,
        )
        doExecute(telegramBotRequest, lookupResult)

    }

    private fun doExecute(
        telegramBotRequest: TelegramBotRequest,
        lookupResult: HandlerLookupResult,
    ) {
        // argumentResolver
        // returnValueHandler
    }
}