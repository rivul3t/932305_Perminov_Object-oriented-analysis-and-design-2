from services.notification_service import NotificationService
from services.telegram_api import TelegramAPI

class TelegramAdapter(NotificationService):
    def __init__(self, telegram: TelegramAPI):
        self.telegram = telegram

    async def send_notification(self, message: str):
        await self.telegram.send_message(message)