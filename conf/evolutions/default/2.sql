# --- !Ups

CREATE TABLE "TASKS" (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    task VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    status VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE "TASKS";