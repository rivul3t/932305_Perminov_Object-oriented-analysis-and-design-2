from services.notification_service import NotificationService
from services.vk_api import VKAPI

class VKAdapter(NotificationService):
    def __init__(self, vk: VKAPI):
        self.vk = vk

    async def send_notification(self, message: str):
        await self.vk.send_message(message)