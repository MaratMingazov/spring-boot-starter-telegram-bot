package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.config.TelegramBotGlobalProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.RequestDispatcher
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

class DefaultTelegramBotUpdatesHandler(
    private val telegramBotGlobalProperties: TelegramBotGlobalProperties,
    private val requestDispatcher: RequestDispatcher,
): TelegramBotUpdatesHandler{

    override fun process(token: String, bot: TelegramBot, updates: List<Update>) {
        updates.forEach { update ->
            telegramBotGlobalProperties.taskExecutor.execute {
                val telegramEvent = TelegramBotEvent.fromUpdate(token, bot, update)
                requestDispatcher.execute(telegramEvent)
            }
        }
    }
}