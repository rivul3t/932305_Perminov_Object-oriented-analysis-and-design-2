from abc import ABC, abstractmethod

class NotificationService(ABC):
    
    @abstractmethod
    async def send_notification(self, message: str):
        pass