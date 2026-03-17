import logging
from logging.handlers import RotatingFileHandler

def setup_logger(filename: str = "notifications.log"):
    logger = logging.getLogger("NotificationApp")
    logger.setLevel(logging.INFO)

    if not logger.handlers:
        console_handler = logging.StreamHandler()
        console_handler.setLevel(logging.INFO)
        console_format = logging.Formatter("[%(asctime)s] [%(levelname)s] %(message)s")
        console_handler.setFormatter(console_format)

        file_handler = RotatingFileHandler(filename, maxBytes=1_000_000, backupCount=5, encoding="utf-8")
        file_handler.setLevel(logging.INFO)
        file_format = logging.Formatter("[%(asctime)s] [%(levelname)s] %(message)s")
        file_handler.setFormatter(file_format)

        logger.addHandler(console_handler)
        logger.addHandler(file_handler)

    return logger

logger = setup_logger()