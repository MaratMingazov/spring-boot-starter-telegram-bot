package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener

class TelegramBotPollingService(
    private val telegramBot: TelegramBot,
): TelegramBotService {
    override fun start() {
        telegramBot.setUpdatesListener { updates ->
            println("updates: ${updates.size}")
            UpdatesListener.CONFIRMED_UPDATES_ALL
        }
    }

    override fun stop() {
        telegramBot.removeGetUpdatesListener()
    }
}