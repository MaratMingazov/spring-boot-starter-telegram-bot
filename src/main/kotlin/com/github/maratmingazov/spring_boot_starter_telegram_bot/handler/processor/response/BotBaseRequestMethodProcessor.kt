package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotBaseRequestMethodProcessor(): BotHandlerMethodReturnValueHandler {

    companion object {
        private val logger = LoggerFactory.getLogger(BotBaseRequestMethodProcessor::class.java)
    }

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        val paramType = returnType.parameterType
        return BaseRequest::class.java.isAssignableFrom(paramType)
    }

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): SendMessage? {
        val paramType = returnType.parameterType
        if (BaseRequest::class.java.isAssignableFrom(paramType)) {
            if (paramType.isInstance(returnValue)) {
                return returnValue as SendMessage
            } else {
                logger.error("Current request is not of type ${paramType.canonicalName}")
            }
        }
        return null
    }
}