# --- !Ups

ALTER TABLE "USERS"
ADD COLUMN "COMPANY_ID" INT;

UPDATE "USERS" SET "COMPANY_ID" = 1 WHERE "ID" = 1;
UPDATE "USERS" SET "COMPANY_ID" = 1 WHERE "ID" = 2;
UPDATE "USERS" SET "COMPANY_ID" = 2 WHERE "ID" = 3;

ALTER TABLE "USERS"
ADD CONSTRAINT FK_COMPANY_ID
FOREIGN KEY ("COMPANY_ID") REFERENCES "COMPANIES"("ID");

# --- !Downs

ALTER TABLE "USERS"
DROP CONSTRAINT "FK_COMPANY_ID";

ALTER TABLE "USERS"
DROP COLUMN "COMPANY_ID";