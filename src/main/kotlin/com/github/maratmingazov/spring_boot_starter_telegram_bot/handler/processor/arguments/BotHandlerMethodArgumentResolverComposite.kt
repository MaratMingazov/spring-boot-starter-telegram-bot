package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotRequest
import org.springframework.core.MethodParameter

class BotHandlerMethodArgumentResolverComposite(
    val argumentResolvers: List<BotHandlerMethodArgumentResolver>,
): BotHandlerMethodArgumentResolver {

    override fun supportsParameters(methodParameter: MethodParameter): Boolean {
        return false
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        telegramBotRequest: TelegramBotRequest
    ): Any {
        return ""
    }
}