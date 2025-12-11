package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.config.TelegramBotGlobalProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.RequestDispatcher
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotCallback
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent
import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import org.slf4j.LoggerFactory
import java.io.IOException

class DefaultTelegramBotUpdatesHandler(
    private val telegramBotGlobalProperties: TelegramBotGlobalProperties,
    private val requestDispatcher: RequestDispatcher,
): TelegramBotUpdatesHandler{

    companion object {
        private val logger = LoggerFactory.getLogger(DefaultTelegramBotUpdatesHandler::class.java)
    }

    override fun process(token: String, bot: TelegramBot, updates: List<Update>) {
        updates.forEach { update ->
            telegramBotGlobalProperties.taskExecutor.execute {
                try {
                    val telegramEvent = TelegramBotEvent.fromUpdate(token, bot, update)
                    val executionResult = requestDispatcher.execute(telegramEvent)
                    if (executionResult != null && executionResult.request != null) {
                        logger.info("Controller returned Telegram Bot Request: ${executionResult.request}")
                        postExecute(executionResult, bot)
                    }
                } catch (e: Exception) {
                    logger.error("Execution error.", e)
                }
            }
        }
    }

    private fun postExecute(executionResult: TelegramBotCallback, bot: TelegramBot) {
        val callback = object : Callback<SendMessage, SendResponse> {
            override fun onResponse(request: SendMessage?, response: SendResponse?) {
                executionResult.onResponse(request, response)
            }

            override fun onFailure(request: SendMessage?, e: IOException?) {
                executionResult.onFailure(request, e)
            }
        }

        bot.execute(executionResult.request, callback)
    }
}