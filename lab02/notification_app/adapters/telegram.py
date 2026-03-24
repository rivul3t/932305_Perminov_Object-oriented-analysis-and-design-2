from services.notification_service import NotificationService
from services.telegram_api import TelegramAPI

class TelegramAdapter(NotificationService):
    def __init__(self, api):
        self.api = api

    async def send_notification(self, message: str) -> dict:
        response = await self.api.send_message(message)

        return {
            "status": "success" if response.get("ok") else "error",
            "service": "Telegram",
            "message_id": response.get("result", {}).get("message_id"),
            "raw": response
        }