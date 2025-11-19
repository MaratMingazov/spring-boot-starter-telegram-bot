package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotController
import com.github.maratmingazov.spring_boot_starter_telegram_bot.config.TelegramBotGlobalProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.config.TelegramBotProperties
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.DefaultRequestMappingsMatcherStrategy
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.DefaultTelegramBotUpdatesHandler
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.HandlerMethodContainer
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.RequestDispatcher
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.RequestMappingsMatcherStrategy
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotPollingService
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotService
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.TelegramBotUpdatesHandler
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolverComposite
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandlerComposite
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import java.util.concurrent.Executors

/**
 * файл org.springframework.boot.autoconfigure.AutoConfiguration.imports
 * Это ServiceLoader-механизм Spring Boot 3+, который заменил старый путь META-INF/spring.factories.
 *
 * Текущая конфигурация подключится в проект и создадутся объявленные бины.
 */
@Configuration
@Import(MethodProcessorsConfiguration::class)
class TelegramBotAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RequestMappingsMatcherStrategy::class)
    fun defaultRequestMappingMatcherStrategy(): RequestMappingsMatcherStrategy {
        return DefaultRequestMappingsMatcherStrategy()
    }

    @Bean
    fun telegramBotGlobalProperties(
        matcherStrategy: RequestMappingsMatcherStrategy,
        argumentResolvers: List<BotHandlerMethodArgumentResolver>,
        returnValueHandlers: List<BotHandlerMethodReturnValueHandler>,
    ): TelegramBotGlobalProperties {
        return TelegramBotGlobalProperties(
            Executors.newSingleThreadExecutor(), // создается non-daemon поток. Поэтому приложение будет работать, пока жив этот поток
                    matcherStrategy,
            argumentResolvers,
            returnValueHandlers,
        )
    }

    @Bean
    @Qualifier("telegramBotPropertiesList")
    fun telegramBotProperties(controllers: List<TelegramBotController>): List<TelegramBotProperties> {
        return controllers.map { controller -> TelegramBotProperties(controller.getToken()) }
    }

    @Bean
    fun telegramBotUpdatesHandler(
        telegramBotGlobalProperties: TelegramBotGlobalProperties,
        requestDispatcher: RequestDispatcher
    ): TelegramBotUpdatesHandler {
        return DefaultTelegramBotUpdatesHandler(telegramBotGlobalProperties, requestDispatcher)
    }

    @Bean
    fun requestDispatcher(
        handlerMethodContainer: HandlerMethodContainer,
        telegramBotGlobalProperties: TelegramBotGlobalProperties,
    ): RequestDispatcher {
        val argumentResolver = BotHandlerMethodArgumentResolverComposite(telegramBotGlobalProperties.argumentResolvers)
        val returnValueHandlers = BotHandlerMethodReturnValueHandlerComposite(telegramBotGlobalProperties.returnValueHandlers)
        return RequestDispatcher(handlerMethodContainer, argumentResolver, returnValueHandlers)
    }

    @Bean
    @Qualifier("telegramBotServices")
    fun telegramBotService(
        @Qualifier("telegramBotPropertiesList") telegramBotProperties: List<TelegramBotProperties>,
        telegramBotUpdatesHandler: TelegramBotUpdatesHandler,
    ): List<TelegramBotService> {
        return telegramBotProperties.map { botProperties ->
            val bot = TelegramBot(botProperties.token)
            TelegramBotPollingService(bot, botProperties, telegramBotUpdatesHandler)
        }
    }

    @Bean
    fun handlerMethodContainer(): HandlerMethodContainer {
        return HandlerMethodContainer()
    }

    @Bean
    fun telegramBotControllerBeanPostProcessor(
        handlerMethodContainer: HandlerMethodContainer
    ): TelegramBotControllerBeanPostProcessor {
        return TelegramBotControllerBeanPostProcessor(handlerMethodContainer)
    }

    /**
     * 	Событие ContextRefreshedEvent публикуется, когда ApplicationContext был создан или обновлён полностью, то есть:
     * 	1.	Все бины были созданы.
     * 	2.	Все зависимости внедрены.
     * 	3.	BeanPostProcessor’ы и все конфигурации выполнены.
     */
    @Bean
    fun onContextRefreshed(
        telegramBotGlobalProperties: TelegramBotGlobalProperties,
        @Qualifier("telegramBotServices") telegramBotServices: List<TelegramBotService>,
        handlerMethodContainer: HandlerMethodContainer,
    ): ApplicationListener<ContextRefreshedEvent> {
        /**
         * Matcher Strategy определяет каким образом мы внутри контроллера находит методы для вызова
         * Если пользователь не задал свою стратегию, мы возьмем default strategy.
         */
        handlerMethodContainer.matcherStrategy = telegramBotGlobalProperties.requestMappingMatcherStrategy
        return ApplicationListener { _ ->
            telegramBotServices.forEach { service -> telegramBotGlobalProperties.taskExecutor.execute { service.start() } }
        }
    }

    @Bean
    fun onContextClosed(
        telegramBotGlobalProperties: TelegramBotGlobalProperties,
        @Qualifier("telegramBotServices") telegramBotServices: List<TelegramBotService>,
    ): ApplicationListener<ContextClosedEvent> {
        return ApplicationListener { _ ->
            telegramBotServices.forEach { it.stop() }
            telegramBotGlobalProperties.taskExecutor.shutdown()
        }
    }

}