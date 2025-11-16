package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotController
import com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation.BotRequest
import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.TelegramBotController
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.RequestMappingInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.SmartInitializingSingleton
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.aop.support.AopUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.annotation.AnnotatedElementUtils;
import java.lang.reflect.Method
import org.springframework.core.MethodIntrospector;

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
         * А нам нужно получить конечный класс, который создал пользователь и повесил на него аннотации
         */
        val targetClass =  AopUtils.getTargetClass(bean)
        if (TelegramBotController::class.java.isAssignableFrom(targetClass) &&
            AnnotationUtils.findAnnotation(targetClass, BotController::class.java) != null) {
            // мы нашли класс, унаследованный от TelegramBotController и с имеющий аннотацию @BotController
            val controller = bean as TelegramBotController
            val annotatedMethods = findAnnotatedMethodsBotRequest(controller.getToken(), targetClass)
            logger.info("Telegram Bot controller for $beanName has been found.")
        }
        return bean
    }

    private fun findAnnotatedMethodsBotRequest(token: String, targetClass: Class<out Any>): Map<Method, List<RequestMappingInfo>> {
        return  MethodIntrospector.selectMethods(
            targetClass,
            MethodIntrospector.MetadataLookup<List<RequestMappingInfo>> { method ->
                // Перебираем все методы найденного контроллера
                // Проверяем есть ли на методы аннотация BotRequest
                val requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, BotRequest::class.java) ?: return@MetadataLookup null
                val types = requestMapping.type.toSet()

                // Если path не указан
                if (requestMapping.path.isEmpty()) {
                    return@MetadataLookup listOf(
                        RequestMappingInfo(token, null, Int.MAX_VALUE, types)
                    )
                }

                // Если есть path
                return@MetadataLookup requestMapping.path.map { path ->
                    RequestMappingInfo(
                        token,
                        path,
                        requestMapping.path.size,
                        types
                    )
                }
            }
        )
    }

    override fun afterSingletonsInstantiated() {
    }
}