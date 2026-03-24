import aiohttp

class DiscordAPI:
    def __init__(self, webhook_url: str):
        self.webhook_url = webhook_url

    async def send_channel_msg(self, text: str):
        payload = {"content": text}
        async with aiohttp.ClientSession() as session:
            async with session.post(self.webhook_url, json=payload) as resp:
                return await resp.json()