CREATE DATABASE wallethub;

CREATE USER 'WalletHubUser'@'%' IDENTIFIED BY 'welcome123';

GRANT ALL ON WalletHub.* to WalletHubUser@'%' IDENTIFIED BY 'welcome123';

USE wallethub;

CREATE TABLE Server_Log (
   ID                        INT NOT NULL AUTO_INCREMENT,
   Log_DateTime              DATETIME NOT NULL,
   Client_IP                 VARCHAR(16) NOT NULL,
   Request_Type              VARCHAR(32) NULL,
   Request_Status            SMALLINT NULL,
   User_Agent                VARCHAR(512) NULL,
   CONSTRAINT Server_Log_PK   PRIMARY KEY(ID)
);

CREATE TABLE Parser_Run (
   Run_ID                    INT NOT NULL,
   Client_IP                 VARCHAR(16) NULL,
   Command                   VARCHAR(128) NULL,
   Comments                  VARCHAR(512) NULL,
   CONSTRAINT Parser_Run_PK  PRIMARY KEY(Run_ID,Client_IP)
);

COMMIT;