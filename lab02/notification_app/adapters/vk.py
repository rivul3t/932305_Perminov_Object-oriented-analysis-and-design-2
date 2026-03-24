from services.notification_service import NotificationService
from services.vk_api import VKAPI

class VKAdapter(NotificationService):
    def __init__(self, api):
        self.api = api

    async def send_notification(self, message: str) -> dict:
        response = await self.api.send_message(message)

        return {
            "status": "success" if "response" in response else "error",
            "service": "VK",
            "message_id": response.get("response"),
            "raw": response
        }