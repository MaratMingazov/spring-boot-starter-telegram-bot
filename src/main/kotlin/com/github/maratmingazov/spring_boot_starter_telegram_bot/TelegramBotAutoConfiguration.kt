package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotController
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.config.TelegramBotGlobalProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.config.TelegramBotProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotPollingService
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotService
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * файл org.springframework.boot.autoconfigure.AutoConfiguration.imports
 * Это ServiceLoader-механизм Spring Boot 3+, который заменил старый путь META-INF/spring.factories.
 *
 * Текущая конфигурация подключится в проект и создадутся объявленные бины.
 */
@Configuration
class TelegramBotAutoConfiguration {

    @Bean
    fun telegramBotGlobalProperties(): TelegramBotGlobalProperties {
        return TelegramBotGlobalProperties()
    }

    @Bean
    @Qualifier("telegramBotPropertiesList")
    fun telegramBotProperties(controllers: List<TelegramBotController>): List<TelegramBotProperties> {
        return controllers.map { controller -> TelegramBotProperties(controller.getToken()) }
    }

    @Bean
    @Qualifier("telegramBotServices")
    fun telegramBotService(
        @Qualifier("telegramBotPropertiesList") telegramBotProperties: List<TelegramBotProperties>
    ): List<TelegramBotService> {
        return telegramBotProperties.map { botProperties ->
            val bot = TelegramBot(botProperties.token)
            TelegramBotPollingService(bot)
        }
    }
}