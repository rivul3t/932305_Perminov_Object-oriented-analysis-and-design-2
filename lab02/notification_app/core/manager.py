from services.notification_service import NotificationService

class NotificationManager:
    def __init__(self):
        self.service = None

    def set_service(self, service):
        self.service = service

    async def notify(self, message: str):
        result = await self.service.send_notification(message)

        if result["status"] == "success":
            print(f"{result['service']} OK, id={result['message_id']}")
        else:
            print(f"pupupu {result['service']} FAILED")

        return result