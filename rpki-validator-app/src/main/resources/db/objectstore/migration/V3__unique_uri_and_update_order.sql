CREATE PROCEDURE DROP_TABLE_IF_EXISTS(IN TABLE_NAME VARCHAR(64))
PARAMETER STYLE JAVA MODIFIES SQL DATA LANGUAGE JAVA EXTERNAL NAME
  'net.ripe.rpki.validator.StoredProcedures.dropTableIfExists';

CALL DROP_TABLE_IF_EXISTS('RETRIEVED_OBJECTS');

CREATE TABLE RETRIEVED_OBJECTS (
  hash VARCHAR(2000) NOT NULL PRIMARY KEY,
  uri VARCHAR(2000) NOT NULL,
  encoded_object VARCHAR(32000) NOT NULL,
  expires TIMESTAMP NOT NULL,
  update_order BIGINT NOT NULL
);

CREATE INDEX uri_idx ON RETRIEVED_OBJECTS(uri, update_order);

CREATE SEQUENCE update_order_seq;

DROP PROCEDURE DROP_TABLE_IF_EXISTS;



