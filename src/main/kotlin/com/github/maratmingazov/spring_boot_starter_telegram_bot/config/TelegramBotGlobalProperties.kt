package com.github.maratmingazov.spring_boot_starter_telegram_bot.config

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.RequestMappingsMatcherStrategy
import java.util.concurrent.ExecutorService

/**
 * Provides global configurations for all telegram bots.
 */
class TelegramBotGlobalProperties(
    val taskExecutor: ExecutorService,
    val requestMappingMatcherStrategy: RequestMappingsMatcherStrategy,
) {
}