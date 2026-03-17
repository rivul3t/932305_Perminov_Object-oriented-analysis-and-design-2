import asyncio
import tkinter as tk
import os
from tkinter import messagebox, simpledialog

from core.manager import NotificationManager
from services.all_services import services
from utils.logger import logger


def run_async(coro):
    asyncio.create_task(coro)

class NotificationApp:
    def __init__(self, root):
        self.root = root
        root.title("Notification App")

        self.manager = NotificationManager()

        tk.Label(root, text="Choose service:").pack()
        self.service_var = tk.StringVar(value="Telegram")
        options = ["Telegram", "Discord", "VK"]
        tk.OptionMenu(root, self.service_var, *options).pack()

        tk.Label(root, text="Enter message:").pack()
        self.message_entry = tk.Entry(root, width=50)
        self.message_entry.pack()

        tk.Button(root, text="Send", command=self.send).pack()

    def send(self):
        message = self.message_entry.get()
        service_name = self.service_var.get()
        self.manager.set_service(services[service_name])
        run_async(self.manager.notify(message))
        messagebox.showinfo("Sent", f"Message sent via {service_name}")


async def main():
    root = tk.Tk()
    app = NotificationApp(root)
    while True:
        root.update()
        await asyncio.sleep(0.01)


if __name__ == "__main__":
    asyncio.run(main())