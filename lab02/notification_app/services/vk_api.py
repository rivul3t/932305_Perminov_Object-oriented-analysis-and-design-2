import aiohttp

class VKAPI:
    def __init__(self, token: str, user_id: str, version: str = "5.131"):
        self.token = token
        self.user_id = user_id
        self.version = version

    async def send_msg(self, text: str):
        pass
        url = "https://api.vk.com/method/messages.send"
        params = {
            "access_token": self.token,
            "user_id": self.user_id,
            "message": text,
            "v": self.version,
            "random_id": 0
        }
        async with aiohttp.ClientSession() as session:
            async with session.post(url, data=params) as resp:
                await resp.text()