CREATE TABLE IF NOT EXISTS users (
     id INTEGER PRIMARY KEY AUTOINCREMENT,
     username TEXT NOT NULL,
     password TEXT NOT NULL,
     num_messages INTEGER NOT NULL,
     last_message_date TEXT
);
