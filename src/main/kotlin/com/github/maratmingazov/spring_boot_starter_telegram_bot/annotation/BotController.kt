package com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation

import org.springframework.stereotype.Component

/**
 * Indicates that an annotated class is a "Controller" (e.g. a web controller).
 *
 * <p>This annotation serves as a specialization of {@link Component @Component},
 * allowing for implementation classes to be autodetected through classpath scanning. It is supposed to used in
 * combination with annotated handler methods based on the {@link BotRequest} annotation.
 *
 * <p><strong>Note:</strong> all classes marked with annotation {@link BotController}
 * must inherit {@link TelegramController}.</p>
 *
 */
@Component
@Target(AnnotationTarget.CLASS) // определили, что аннотацию можно применять только на классы
@Retention(AnnotationRetention.RUNTIME) // определяет, что аннотация попадет в bytecode и будет доступна в RUNTIME через reflection. Это нужно, потому что Spring читает аннотации через reflection.
annotation class BotController()
