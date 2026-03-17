from services.notification_service import NotificationService
from services.discord_api import DiscordAPI

class DiscordAdapter(NotificationService):
    def __init__(self, discord: DiscordAPI):
        self.discord = discord

    async def send_notification(self, message: str):
        await self.discord.send_channel_msg(message)