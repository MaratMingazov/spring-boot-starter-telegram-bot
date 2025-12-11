package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import org.springframework.core.BridgeMethodResolver
import org.springframework.core.MethodParameter
import org.springframework.core.ResolvableType
import org.springframework.core.annotation.SynthesizingMethodParameter
import org.springframework.util.ClassUtils
import java.lang.reflect.Method

/**
 * При запуске приложения мы будем перебирать все бины у которых есть аннотация @BotController
 * Это наши классы контроллеры.
 * Потом у этих классов будем перебирать все методы, которых есть анноатция @BotRequest
 * Для каждого такого найденного метода мы должны создать экземпляр класса HandlerMethod.
 */
open class HandlerMethod(
    protected val bean: Any,
    private val method: Method,
) {

    val beanType: Class<*> = ClassUtils.getUserClass(bean)

    /**
     * Сам метод помеченный аннотацией @BotRequest может быть обернут в прокси
     * Поэтому здесь нам нужно найти искомый метод,
     */
    val bridgedMethod: Method = BridgeMethodResolver.findBridgedMethod(method)

    /**
     * Для @botRequest метода мы храним массив его параметров
     * Например, для метода
     *      @BotRequest(value=arrayOf("/start"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
     *      fun hello(request: TelegramBotRequest, user: User, chat: Chat) {
     *      }
     * Мы тут будем хранить массив параметров [TelegramBotRequest, User, Chat]
     */
    val methodParameters: Array<MethodParameter> = initMethodParameters()

    constructor(handlerMethod: HandlerMethod) : this(handlerMethod.bean, handlerMethod.method) {
    }

    fun getReturnValue(returnValue: Any?): MethodParameter {
        return ReturnValueMethodParameter(returnValue, bridgedMethod)
    }


    /**
     * Во время запуска приложения мы перебираем все классы контроллеры и у них методы с аннотацией @BotRequest
     * У этих методов могут быть параметры метода.
     * Мы должны их тут определить, чтобы потом можно было их передать во время вызова метода
     * Например, если метод определен таким образом
     *     @BotRequest(value=arrayOf("/start"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
     *     fun hello(request: TelegramBotRequest, user: User, chat: Chat) {
     *     }
     *  То мы тут найдем и сохраним 3 параметра: TelegramBotRequest, User, Chat
     */
    private fun initMethodParameters(): Array<MethodParameter> {
        val count = bridgedMethod.parameterCount
        val result = arrayOfNulls<MethodParameter>(count)

        for (i in 0 until count) {
            val parameter = SynthesizingMethodParameter(bridgedMethod, i)

            // Новый способ резолвинга generic-типа
            ResolvableType
                .forMethodParameter(parameter)
                .resolve(beanType)

            result[i] = parameter
        }

        return  result.requireNoNulls()
    }

    fun simpleName(): String {
        return "${bridgedMethod.declaringClass.typeName}.${bridgedMethod.name}"
    }
}

class ReturnValueMethodParameter(
    private val returnValue: Any?,
    private val bridgedMethod: Method,
): MethodParameter(bridgedMethod, -1) {

    override fun getParameterType(): Class<*> {
        return returnValue?.javaClass ?: super.getParameterType()
    }

}