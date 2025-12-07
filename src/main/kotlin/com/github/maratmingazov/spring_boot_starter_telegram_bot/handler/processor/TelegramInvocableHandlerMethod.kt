package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.util.ReflectionUtils
import java.lang.reflect.InvocationTargetException

class TelegramInvocableHandlerMethod(
    private val handlerMethod: HandlerMethod,
    private val argumentResolver: BotHandlerMethodArgumentResolver,
    private val returnValueHandler: BotHandlerMethodReturnValueHandler,
    ): HandlerMethod(handlerMethod) {

    private val parameterNameDiscoverer = DefaultParameterNameDiscoverer()

    fun invokeAndHandle(telegramBotRequest: TelegramBotRequest): BaseRequest<*, *>? {
        /**
         * Мы значит хотим вызвать метод, помеченный аннотацией @BotRequest
         * У этого метода мы знаем параметры.
         * Мы должны определеить какие значения будем передавать в эти параметры
         * Для этого мы просто захардкодили, что в зависимости от типа параметра, мы передаем определенное значение
         */
        val args = getMethodArgumentValues(telegramBotRequest)

        /**
         * теперь собственное нужно вызвать метод и передать туда эти аргументы
         */
        val result = doSafeInvoke(args)


        throw RuntimeException("")
    }

    /**
     * Мы вызываем метод пользователя и передаем туда аргументы
     */
    private fun doSafeInvoke(args:  Array<Any?>): Any {
        ReflectionUtils.makeAccessible(bridgedMethod)
        try {
            val result = bridgedMethod.invoke(bean, *args)
            /**
             *  Результат это то, что возвращает пользовательский метод
             */
            return result
        } catch (ex: InvocationTargetException) {
            val targetException: Throwable? = ex.getTargetException()
            val text = "Failed to invoke handler method $args"
            throw IllegalStateException(text, targetException)
        } catch (ex: Exception) {
            val text = "message: ${ex.message}, args: $args"
            throw IllegalStateException(text, ex)
        }
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

        /**
         * Тут мы для каждого параметра должны подобрать знечение, которые мы будем передавать в метод
         */
        val arguments = parameters.map { parameter ->
            parameter.initParameterNameDiscovery(parameterNameDiscoverer)
            val argument = argumentResolver.resolveArgument(parameter, telegramBotRequest)
            argument
        }
        return arguments.toTypedArray()
    }
}