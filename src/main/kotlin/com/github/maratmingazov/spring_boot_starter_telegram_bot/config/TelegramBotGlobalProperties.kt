package com.github.maratmingazov.spring_boot_starter_telegram_bot.config

import java.util.concurrent.ExecutorService

/**
 * Provides global configurations for all telegram bots.
 */
class TelegramBotGlobalProperties(
    val taskExecutor: ExecutorService,
) {
}