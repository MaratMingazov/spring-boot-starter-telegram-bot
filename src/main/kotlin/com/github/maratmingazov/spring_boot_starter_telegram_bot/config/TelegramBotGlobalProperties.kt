package com.github.maratmingazov.spring_boot_starter_telegram_bot.config

import java.util.concurrent.ThreadPoolExecutor

/**
 * Provides global configurations for all telegram bots.
 */
class TelegramBotGlobalProperties(
    val taskExecutor: ThreadPoolExecutor,
) {
}