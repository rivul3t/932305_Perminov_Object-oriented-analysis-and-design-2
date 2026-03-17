from services.notification_service import NotificationService

class NotificationManager:
    def __init__(self):
        self.service: NotificationService | None = None

    def set_service(self, service: NotificationService):
        self.service = service

    async def notify(self, message: str):
        if not self.service:
            print("Notification service not selected")
            return
        await self.service.send_notification(message)
        print("Notification sent")