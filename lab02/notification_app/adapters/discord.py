from services.notification_service import NotificationService
from services.discord_api import DiscordAPI

class DiscordAdapter(NotificationService):
    def __init__(self, api):
        self.api = api

    async def send_notification(self, message: str) -> dict:
        status_code = await self.api.send_channel_message(message)

        return {
            "status": "success" if status_code == 204 else "error",
            "service": "Discord",
            "message_id": None,
            "raw": status_code
        }