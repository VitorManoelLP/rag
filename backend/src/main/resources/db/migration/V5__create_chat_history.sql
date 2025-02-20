CREATE TABLE IF NOT EXISTS chat_history (
                              id UUID PRIMARY KEY,
                              user_message TEXT,
                              bot_message TEXT,
                              created_at TIMESTAMP,
                              updated_at TIMESTAMP
);