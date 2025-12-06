package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter

class BotHandlerMethodArgumentResolverComposite(
    val argumentResolvers: List<BotHandlerMethodArgumentResolver>,
): BotHandlerMethodArgumentResolver {

    companion object {
        private val logger = LoggerFactory.getLogger(BotRequestMethodArgumentResolver::class.java)
    }

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        return false
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any? {
        val resolver = getArgumentResolver(methodParameter)
        if (resolver == null) {
            logger.error("Unknown parameter type [" + methodParameter.getParameterType().getName() + "]")
            return null
        }
        return resolver.resolveArgument(methodParameter, telegramBotRequest)
    }

    private fun getArgumentResolver(methodParameter: MethodParameter): BotHandlerMethodArgumentResolver? {
        return argumentResolvers.firstOrNull { it.supportsParameters(methodParameter) }
    }
}