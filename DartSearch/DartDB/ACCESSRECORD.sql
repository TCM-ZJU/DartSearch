drop table ACCOUNTFEE;
create table ACCOUNTFEE
(
  ACCOUNTID INT,
  FEE       INT,
  INTTIME   LONG,
  TIME      VARCHAR(100),
  TYPE      INT,
  CONTENT   VARCHAR(512),
  GOODSID   VARCHAR(512)
);

drop table  ACCOUNT;
create table ACCOUNT
(
  LOGINTYPE      VARCHAR2(50) not null,
  USERNAME       VARCHAR2(50),
  PASSWORD       VARCHAR2(50),
  STATUS         VARCHAR2(50) not null,
  IPADDRESS      VARCHAR2(100),
  MASK           VARCHAR2(100),
  SUBNETADDRESS  VARCHAR2(100),
  CHARGETYPE     VARCHAR2(50),
  STARTDATE      VARCHAR2(50),
  TOTALDATE      INTEGER default 0,
  TOTALFLOW      INTEGER default 0,
  USEDFLOW       INTEGER default 0,
  MAXCONCURRENCE INTEGER default 0,
  ACCID          INTEGER default 1 not null,
  GROUPID        VARCHAR2(50),
  EVENT          VARCHAR2(100)
);

drop table  AUTHORITIES;
create table AUTHORITIES
(
  AUTHID    INTEGER not null,
  AUTHORITY VARCHAR(100) not null,
  AUTHTYPE  VARCHAR(20),
  RES       VARCHAR(100),
  DISPLAY   VARCHAR(100)
);


drop table  TABLEACCESSPRIV;
create table TABLEACCESSPRIV
(
  ACCOUNTID  INTEGER not null,
  TABLEOWNER VARCHAR(20),
  TABLENAME  VARCHAR(32),
  READPRIV   INTEGER,
  WANTREADPRIV  INTEGER
);

drop table  ONTOLOGYACCESSPRIV;
create table ONTOLOGYACCESSPRIV
(
 AccountId   INTEGER not null,
 OntologyURI VARCHAR(128),
 ReadPriv    INTEGER,
 WANTREADPRIV  INTEGER,
 IsCategory  INTEGER
);

drop table  TABLEPRICE;
Create table TABLEPRICE
(
 TableId     VARCHAR(128),
 FreeCharge  INTEGER default 0,
 Price       INTEGER default 0 
);


drop table  USERINFO;
create table USERINFO
(
  ID           INTEGER default 1 not null,
  CONTACT_NAME VARCHAR(50) ,
  SEX          VARCHAR(10) ,
  CONTACT      VARCHAR(50) 
);


Create Table TableNameLocalization
( 
TableID   VARCHAR(256) not null,
ChineseID VARCHAR(256) 
)
