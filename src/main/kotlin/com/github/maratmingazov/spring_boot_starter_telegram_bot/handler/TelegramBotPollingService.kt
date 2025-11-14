package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.config.TelegramBotProperties
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener

class TelegramBotPollingService(
    private val telegramBot: TelegramBot,
    private val telegramBotProperties: TelegramBotProperties,
    private val telegramBotUpdatesHandler: TelegramBotUpdatesHandler,
): TelegramBotService {
    override fun start() {
        telegramBot.setUpdatesListener { updates ->
            telegramBotUpdatesHandler.process(telegramBotProperties.token, telegramBot, updates)
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    override fun stop() {
        telegramBot.removeGetUpdatesListener()
    }
}