package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.core.DefaultParameterNameDiscoverer

class TelegramInvocableHandlerMethod(
    private val handlerMethod: HandlerMethod,
    private val argumentResolver: BotHandlerMethodArgumentResolver,
    private val returnValueHandler: BotHandlerMethodReturnValueHandler,
    ): HandlerMethod(handlerMethod) {

    private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()

    fun invokeAndHandle(telegramBotRequest: TelegramBotRequest): BaseRequest<*, *>? {
        val args = getMethodArgumentValues(telegramBotRequest)
        throw RuntimeException("")
    }

    /**
     * У метода, который мы собираемся вызвать, могут быть параметры.
     * В этом случае нужно понять, какие значения туда передать
     * Поэтому мы сначала должны получить параметры этого метода,
     * А потом понять, какую значения будем туда передавать
     * Этот метод должен вернуть список значений, который мы будем передавать в метод
     */
    private fun getMethodArgumentValues(telegramBotRequest: TelegramBotRequest): Array<Any?> {
        /**
         * Смотрим есть ли у метода (который нам нужно вызвать) входные параметры
         * Например, если это такой метод
         *      @BotRequest(value=arrayOf("/start"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
         *      fun hello(request: TelegramBotRequest, user: User, chat: Chat) {}
         * То у него тут будем массив из 3 параметров: TelegramBotRequest, User, Chat
         */
        val parameters = methodParameters

        val arguments = parameters.map { parameter ->
            parameter.initParameterNameDiscovery(parameterNameDiscoverer)
            val argument = argumentResolver.resolveArgument(parameter, telegramBotRequest)
            argument
        }
        return arguments.toTypedArray()
    }
}