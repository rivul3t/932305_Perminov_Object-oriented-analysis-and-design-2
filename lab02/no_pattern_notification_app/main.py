import asyncio
import tkinter as tk
from tkinter import messagebox, simpledialog

from services.telegram_api import TelegramAPI
from services.discord_api import DiscordAPI
from services.vk_api import VKAPI
from utils.logger import logger

def run_async(coro):
    asyncio.create_task(coro)


class NotificationApp:
    def __init__(self, root):
        self.root = root
        root.title("Notification App No Adapter")

        tk.Label(root, text="Choose service:").pack()
        self.service_var = tk.StringVar(value="Telegram")
        options = ["Telegram", "Discord", "VK"]
        tk.OptionMenu(root, self.service_var, *options).pack()

        tk.Label(root, text="Enter message:").pack()
        self.message_entry = tk.Entry(root, width=50)
        self.message_entry.pack()

        tk.Button(root, text="Send", command=self.send).pack()

    def send(self):
        service_name = self.service_var.get()
        message = self.message_entry.get()

        if not message:
            messagebox.showerror("Error", "Message cannot be empty")
            return

        if service_name == "Telegram":
            bot_token = os.getenv("TELEGRAM_BOT_TOKEN")
            chat_id = os.getenv("TELEGRAM_CHAT_ID")
            if not bot_token or not chat_id:
                messagebox.showerror("Error", "Telegram credentials not set in .env")
                return
            api = TelegramAPI(bot_token, chat_id)
            run_async(self.send_telegram(api, message))

        elif service_name == "Discord":
            webhook = os.getenv("DISCORD_WEBHOOK_URL")
            if not webhook:
                messagebox.showerror("Error", "Discord webhook not set in .env")
                return
            api = DiscordAPI(webhook)
            run_async(self.send_discord(api, message))

        elif service_name == "VK":
            token = os.getenv("VK_GROUP_TOKEN")
            peer_id = os.getenv("VK_PEER_ID")
            if not token or not peer_id:
                messagebox.showerror("Error", "VK credentials not set in .env")
                return
            api = VKAPI(token, peer_id)
            run_async(self.send_vk(api, message))

        else:
            messagebox.showerror("Error", "Unknown service")
            return

        messagebox.showinfo("Sent", f"Message sent via {service_name}")

    async def send_telegram(self, api: TelegramAPI, message: str):
        try:
            await api.send_message(message)
            logger.info(f"Telegram: {message}")
        except Exception as e:
            logger.error(f"Telegram error: {e}")

    async def send_discord(self, api: DiscordAPI, message: str):
        try:
            await api.send_channel_message(message)
            logger.info(f"Discord: {message}")
        except Exception as e:
            logger.error(f"Discord error: {e}")

    async def send_vk(self, api: VKAPI, message: str):
        try:
            await api.send_message(message)
            logger.info(f"VK: {message}")
        except Exception as e:
            logger.error(f"VK error: {e}")

async def main():
    root = tk.Tk()
    app = NotificationApp(root)
    while True:
        root.update()
        await asyncio.sleep(0.01)


if __name__ == "__main__":
    asyncio.run(main())