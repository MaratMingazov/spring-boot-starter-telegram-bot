package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.HandlerMethodContainer
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotEvent

class RequestDispatcher(
    private val handlerMethodContainer: HandlerMethodContainer,
) {

    /**
     * Должен найти контроллер (помеченный как @BotController) и у этого контроллера вызвать правильный метод (помеченный @BotRequest)
     */
    fun execute(telegramEvent: TelegramBotEvent) {
        // Нам нужно найти метод, в который мы передадим этот запрос
        val lookupResult = handlerMethodContainer.lookupHandlerMethod(telegramEvent)
        val handlerMethod = lookupResult.handlerMethod
    }
}