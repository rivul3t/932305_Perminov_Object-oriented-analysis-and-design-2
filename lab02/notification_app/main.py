import asyncio
import tkinter as tk
import os
from tkinter import messagebox, simpledialog

from core.manager import NotificationManager
from adapters.all_adapters import adapters
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

        self.manager.set_service(adapters[service_name])
        asyncio.create_task(self.send_async(message))

    async def send_async(self, message):
        result = await self.manager.notify(message)
        self.root.after(0, self.show_result, result)

    def show_result(self, result):
        if result["status"] == "success":
            messagebox.showinfo(
                "Success",
                f"{result['service']} OK\nmessage_id: {result['message_id'] or 'N/A'}"
            )
        else:
            messagebox.showerror(
                "Error",
                f"{result['service']} FAILED"
            )



async def main():
    root = tk.Tk()
    app = NotificationApp(root)
    while True:
        root.update()
        await asyncio.sleep(0.01)


if __name__ == "__main__":
    asyncio.run(main())