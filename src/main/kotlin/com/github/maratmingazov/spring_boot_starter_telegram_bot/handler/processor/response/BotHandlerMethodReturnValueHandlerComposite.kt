package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.SendMessage
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotHandlerMethodReturnValueHandlerComposite(
    private val handlers: List<BotHandlerMethodReturnValueHandler>,
): BotHandlerMethodReturnValueHandler {

    companion object {
        private val logger = LoggerFactory.getLogger(BotHandlerMethodReturnValueHandlerComposite::class.java)
    }

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        return getReturnValueHandler(returnType) != null
    }

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): SendMessage? {
        val handler = getReturnValueHandler(returnType)
        if (handler == null) {
            logger.error("Unknown return value type: " + returnType.parameterType.getName());
            return null
        }
        return handler.handleReturnValue(returnValue, returnType, telegramBotRequest)
    }

    private fun getReturnValueHandler(returnType: MethodParameter): BotHandlerMethodReturnValueHandler? {
        return handlers.firstOrNull { it.supportsReturnType(returnType) }
    }
}