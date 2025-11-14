package com.github.maratmingazov.spring_boot_starter_telegram_bot

import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanPostProcessor

/**
 * Searches for {@link TelegramMvcController} inheritors marked with {BotController} annotation, then searches for
 * {BotRequest} annotations in methods and store the meta
 * information into {HandlerMethodContainer}.
 */
class TelegramBotControllerBeanPostProcessor: BeanPostProcessor, SmartInitializingSingleton {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        return bean
    }

    override fun afterSingletonsInstantiated() {
    }
}