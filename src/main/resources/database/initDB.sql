CREATE TABLE IF NOT EXISTS "public"."updates" (
    "update_id" INT,
    "username" TEXT,
    "message_timestamp" TIMESTAMP WITH TIME ZONE NOT NULL,
    "message_text" TEXT,
    "chat_id" BIGINT,
    PRIMARY KEY ("update_id")

);