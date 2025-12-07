package com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation

/**
 * Annotation which indicates that a method parameter should be bound to a request template variable. Supported for
 * [BotRequest] annotated handler methods.
 *
 *
 * **Note:** Works only if and when the method parameter is [String].
 *
 * @see BotRequest
 *
 * @see com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.arguments.BotRequestMethodPathArgumentResolver
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BotPathVariable(
    /**
     * Name of the template variable that should be bound to a method parameter.
     * If no name is set, variable name will be used instead.
     */
    val value: String = ""
)