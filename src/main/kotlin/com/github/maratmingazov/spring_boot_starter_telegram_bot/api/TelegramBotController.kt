package com.github.maratmingazov.spring_boot_starter_telegram_bot.api

/**
 * An interface from which all classes marked with annotation {@link BotRequest} must inherit.
 *
 * @see BotRequest
 * @see TelegramBotControllerBeanPostProcessor
 */
interface TelegramBotController {

    fun getToken(): String
}