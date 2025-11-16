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
class TelegramBotControllerBeanPostProcessor(
    private val handlerMethodContainer: HandlerMethodContainer
): BeanPostProcessor, SmartInitializingSingleton {

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
            // Мы нашли класс, унаследованный от TelegramBotController и с имеющий аннотацию @BotController
            val controller = bean as TelegramBotController
            // находим методы, которые помечены аннотацией @BotRequest
            val annotatedMethods = findAnnotatedMethodsBotRequest(controller.getToken(), targetClass)
            annotatedMethods.forEach { (method, mappingInfos) ->
                val invocableMethod = AopUtils.selectInvocableMethod(method, targetClass)
                // Осталось запомнить наш контроллер и метод
                handlerMethodContainer.registerController(bean, invocableMethod, mappingInfos)
                logger.info("Telegram Bot controller  $beanName / ${invocableMethod.name} has been found and registered.")
            }

        }
        return bean
    }

    private fun findAnnotatedMethodsBotRequest(token: String, targetClass: Class<out Any>): Map<Method, List<RequestMappingInfo>> {
        return  MethodIntrospector.selectMethods(
            targetClass,
            MethodIntrospector.MetadataLookup<List<RequestMappingInfo>> { method ->
                // Перебираем все методы найденного контроллера
                // Проверяем есть ли на методе аннотация @BotRequest(value=arrayOf("/hello"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
                val requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, BotRequest::class.java) ?: return@MetadataLookup null
                // Смотрим какие MessageTypes прописаны у этой анноатации  type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE)
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
                        path, // "/hello"
                        requestMapping.path.size,
                        types // arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE)
                    )
                }
            }
        )
    }

    override fun afterSingletonsInstantiated() {
    }
}