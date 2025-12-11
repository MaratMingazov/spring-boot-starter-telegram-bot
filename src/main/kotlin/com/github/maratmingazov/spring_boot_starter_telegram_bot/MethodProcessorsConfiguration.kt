package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotHandlerMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotRequestMethodArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotRequestMethodPathArgumentResolver
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotBaseRequestMethodProcessor
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotHandlerMethodReturnValueHandler
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.response.BotResponseBodyMethodProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

@Configuration
class MethodProcessorsConfiguration {

    @Bean
    fun botRequestMethodArgumentResolver(): BotHandlerMethodArgumentResolver {
        return BotRequestMethodArgumentResolver()
    }

    @Bean
    fun botRequestMethodPathArgumentResolver(): BotHandlerMethodArgumentResolver {
        return BotRequestMethodPathArgumentResolver()
    }

    @Bean
    fun botBaseRequestMethodProcessor(): BotHandlerMethodReturnValueHandler {
        return BotBaseRequestMethodProcessor()
    }

    @Bean
    fun botResponseBodyMethodProcessor(
        conversionService: ConversionService
    ): BotHandlerMethodReturnValueHandler {
        return BotResponseBodyMethodProcessor(conversionService)
    }

    @Bean
    fun conversionService(): ConversionService {
        val bean = ConversionServiceFactoryBean()
        bean.afterPropertiesSet()
        return  bean.`object`!!
    }
}