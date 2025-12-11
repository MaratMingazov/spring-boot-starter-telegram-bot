package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.core.MethodParameter
import org.springframework.core.convert.ConversionService

class BotResponseBodyMethodProcessor(
    private val conversionService: ConversionService
): BotHandlerMethodReturnValueHandler {

    override fun supportsReturnType(returnType: MethodParameter): Boolean {
        return true
    }

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): SendMessage? {
        var outputValue: String? = null
        if (returnValue is CharSequence) {
            outputValue = returnValue.toString()
        } else {
            val valueType = if (returnValue != null)
                returnValue.javaClass
            else
                returnType.parameterType
            if (conversionService.canConvert(valueType, String::class.java)) {
                outputValue = conversionService.convert(returnValue, String::class.java)
            }
        }
        if (outputValue != null) {
            if (telegramBotRequest.chat != null) {
                val request = SendMessage(telegramBotRequest.chat.id(), outputValue)
                return request
            }
        }
        return null
    }
}