package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.HandlerMethodContainer
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.pengrad.telegrambot.request.BaseRequest
import org.slf4j.LoggerFactory

class RequestDispatcher(
    private val handlerMethodContainer: HandlerMethodContainer,
    private val argumentResolver: BotHandlerMethodArgumentResolver,
    private val returnValueHandler: BotHandlerMethodReturnValueHandler,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RequestDispatcher::class.java)
    }

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
        lookupResult.handlerMethod?.let { handlerMethod ->
            try {
                doExecute(telegramBotRequest, handlerMethod)
            } catch (e: Exception) {
                logger.error("Exception while executing TelegramEvent request", e)
            }
        } ?: run {
            logger.info("Controller not found for ${telegramEvent.text}, type: ${telegramEvent.messageType}")
        }

    }

    private fun doExecute(
        request: TelegramBotRequest,
        handlerMethod: HandlerMethod,
    ): BaseRequest<*,*>? {
        val result = TelegramInvocableHandlerMethod(handlerMethod, argumentResolver, returnValueHandler).invokeAndHandle()
        logger.info("${request.messageType} request was executed by ${handlerMethod.simpleName()} with ${result?.let { it.javaClass.simpleName } ?: "null"} result")
        return result
    }
}