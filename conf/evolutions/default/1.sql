# --- !Ups

CREATE TABLE "USERS" (
    username VARCHAR NOT NULL PRIMARY KEY,
    password VARCHAR NOT NULL,
    firstName VARCHAR NOT NULL,
    lastName VARCHAR NOT NULL,
    email VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE "USERS";