from dotenv import load_dotenv
from services.discord_api import DiscordAPI
from services.telegram_api import TelegramAPI
from services.vk_api import VKAPI

from adapters.discord import DiscordAdapter
from adapters.telegram import TelegramAdapter
from adapters.vk import VKAdapter

import os

load_dotenv()

adapters = {
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