package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotPathVariable
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotRequestMethodPathArgumentResolver(): BotHandlerMethodArgumentResolver {

    companion object {
        private val logger = LoggerFactory.getLogger(BotRequestMethodPathArgumentResolver::class.java)
    }

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        val paramType = methodParameter.parameterType
        return methodParameter.hasParameterAnnotation(BotPathVariable::class.java) &&
                String::class.java.isAssignableFrom(paramType) ||
                Int::class.java.isAssignableFrom(paramType)


    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any? {
        /**
         * Пользователь объявил метод и для параметра указал аннотация @BotPathVariable
         *
         *     @BotRequest(value=arrayOf("/hello {name:[\\S]+}"))
         *     fun helloWithName(@BotPathVariable("name") userName: String): String {
         *         return "hi $userName"
         *     }
         *
         */

        /**
         * Определяем тип этого параметра, в данном случае String
         */
        val paramType = methodParameter.parameterType


        /**
         * Проверяем что у него есть аннотация @BotPathVariable
         * В данном случае (@BotPathVariable("name")
         */
        val annotation = methodParameter.getParameterAnnotation(BotPathVariable::class.java)
        if (telegramBotRequest.templateVariables == null || annotation == null) {
            // nothing to extract
            return null
        }
        val value = if (annotation.value.isEmpty()) {
            methodParameter.parameterName?.let{ telegramBotRequest.templateVariables[it] }
        } else {
            /**
             * annotation.value - это переменная, которую мы прописали в аннотации, в данном случае "name"
             * и нам теперь из запроса нужно достать значение этой переменной
             * Если например пользователь в бот напишет текст "/hello name:Marat"
             * Тут у нас будет map name -> name:Marat
             */
            telegramBotRequest.templateVariables[annotation.value]
        }

        if (value == null) {
            return null
        }

        try {
            if (String::class.java.isAssignableFrom(paramType)) {
                return validateValue(paramType, value)
            }
            if (Int::class.java.isAssignableFrom(paramType)) {
                return validateValue(paramType, value.toInt())
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }

        return null
    }

    private fun validateValue(paramType: Class<*>, value: Any?): Any? {
        if (value != null && !paramType.isInstance(value)) {
            logger.error("Current request is not of type [" + paramType.getName() + "]: " + value + "")
            return null
        }
        return value
    }
}