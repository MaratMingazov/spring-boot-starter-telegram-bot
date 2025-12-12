# spring-boot-starter-telegram-bot

### Gradle

```
implementation("com.github.maratmingazov:spring-boot-starter-telegram-bot:1.0.0")
```

### Example
The only thing you need to do after adding the dependency is to create a bot controller
```kotlin
@BotController
class AITelegramBotController(
    private val properties: TelegramBotProperties
): TelegramBotController {

    override fun getToken(): String {
        return properties.token
    }

    @BotRequest(value=arrayOf("/start"), type=arrayOf(MessageType.MESSAGE))
    fun hello(request: TelegramBotRequest, str: String): String {
        return "hi"
    }

    @BotRequest(value=arrayOf("/hello {name:[\\S]+}"))
    fun helloWithName(@BotPathVariable("name") userName: String): String {
        return "hi $userName"
    }

    @BotRequest(type=arrayOf(MessageType.MESSAGE))
    fun default(request: TelegramBotRequest, user: User, chat: Chat, update: Update, message: Message) : SendMessage {
        val value = request.text
        return SendMessage(chat.id(), "Hello ${user.firstName()}, ${value}!")
    }
}
```
The bot will be registered automatically on startup.