from dotenv import load_dotenv

load_dotenv()

services = {
        "Telegram": TelegramAdapter(TelegramAPI(
            os.getenv("TELEGRAM_BOT_TOKEN"),
            os.getenv("TELEGRAM_CHAT_ID")
        )),
        "Discord": DiscordAdapter(DiscordAPI(
            os.getenv("DISCORD_WEBHOOK_URL")
        )),
        "VK": VKAdapter(VKAPI(
            os.getenv("VK_GROUP_TOKEN"),
            os.getenv("VK_PEER_ID")
        ))
}