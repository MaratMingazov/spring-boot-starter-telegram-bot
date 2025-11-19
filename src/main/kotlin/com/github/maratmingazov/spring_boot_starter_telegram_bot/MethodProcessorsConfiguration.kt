package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotRequestMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotRequestMethodPathArgumentResolver
import org.springframework.context.annotation.Configuration

@Configuration
class MethodProcessorsConfiguration {

    fun botRequestMethodArgumentResolver(): BotHandlerMethodArgumentResolver {
        return BotRequestMethodArgumentResolver()
    }

    fun botRequestMethodPathArgumentResolver(): BotHandlerMethodArgumentResolver {
        return BotRequestMethodPathArgumentResolver()
    }
}