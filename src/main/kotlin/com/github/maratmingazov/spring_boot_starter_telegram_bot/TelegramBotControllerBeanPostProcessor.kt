package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotController
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils

/**
 * Searches for {@link TelegramMvcController} inheritors marked with {BotController} annotation, then searches for
 * {BotRequest} annotations in methods and store the meta
 * information into {HandlerMethodContainer}.
 */
class TelegramBotControllerBeanPostProcessor(): BeanPostProcessor, SmartInitializingSingleton {

    companion object {
        private val logger = LoggerFactory.getLogger(TelegramBotControllerBeanPostProcessor::class.java)
    }
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {

        /**
         * Spring может оборачивать классы в Proxy классы.
         * А нам получить конечный класс, который создал пользователь и повесил на него аннотации
         */
        val targetClass =  AopUtils.getTargetClass(bean)
        if (TelegramBotController::class.java.isAssignableFrom(targetClass) &&
            AnnotationUtils.findAnnotation(targetClass, BotController::class.java) != null) {
            // мы нашли класс, унаследованный от TelegramBotController и с имеющий аннотацию @BotController
            val controller = bean as TelegramBotController
            logger.info("Telegram Bot controller for $beanName has been found. $controller. ${controller.getToken()}")
        }
        return bean
    }

    override fun afterSingletonsInstantiated() {
    }
}