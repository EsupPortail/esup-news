ALTER TABLE news_user DROP COLUMN user_name;
ALTER TABLE news_user DROP COLUMN email;
ALTER TABLE news_item DROP COLUMN post_user_name;

--CREATE SEQUENCE news_entity_entity_id_seq;

CREATE TABLE news_entity (
  entity_id bigserial NOT NULL,
  name VARCHAR(250) UNIQUE,
  description TEXT,
  display_priority INTEGER default 0,
  created_by VARCHAR(50) NOT NULL,
  creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (entity_id)
);

--CREATE SEQUENCE news_type_type_id_seq;

CREATE TABLE news_type (
  type_id bigserial NOT NULL,
  name VARCHAR(250) UNIQUE,
  description TEXT,
  PRIMARY KEY (type_id)
);

INSERT INTO news_type (type_id,name, description) VALUES 
(1,'DEFAULT', 'Type de catégorie par défaut.');

--CREATE SEQUENCE news_filter_filter_id_seq;

CREATE TABLE news_filter (
  filter_id bigserial NOT NULL,
  attribute VARCHAR(250) NOT NULL,
  operator VARCHAR(10) NOT NULL,
  value VARCHAR(250) NOT NULL default '',
  type VARCHAR(50) NOT NULL,
  entity_id BIGINT NOT NULL,
  PRIMARY KEY (filter_id),
  CONSTRAINT filter_entity_fk FOREIGN KEY (entity_id) REFERENCES news_entity(entity_id),
  CONSTRAINT unique_filter UNIQUE (attribute, operator, value, type, entity_id)
);

-- Due to the migration it's missing the contraint Not Null for the entity_id, 
-- when all categories will be associated to an entity it must be added.

ALTER TABLE news_category 
  ADD COLUMN entity_id BIGINT,
  ADD CONSTRAINT  cat_entity_fk   FOREIGN KEY (entity_id) REFERENCES news_entity(entity_id);

CREATE TABLE news_entity_type (
       entity_id BIGINT NOT NULL,
       type_id BIGINT NOT NULL,
       PRIMARY KEY (entity_id, type_id),
       CONSTRAINT entity_type_entity_fk FOREIGN KEY (entity_id)
                  REFERENCES news_entity (entity_id),
       CONSTRAINT entity_type_type_fk FOREIGN KEY (type_id)
                  REFERENCES news_type (type_id)
);

CREATE TABLE news_category_type (
       cat_id BIGINT NOT NULL, 
       type_id BIGINT NOT NULL, 
       PRIMARY KEY (cat_id, type_id),
       CONSTRAINT category_type_cat_fk FOREIGN KEY (cat_id)
                  REFERENCES news_category (cat_id),
       CONSTRAINT category_type_type_fk FOREIGN KEY (type_id)
                  REFERENCES news_type (type_id)
);

CREATE TABLE news_attachment (
  attachment_id  INTEGER NOT NULL ,
  cmis_uid VARCHAR(250) default '',
  file_name VARCHAR(100) default '',
  title VARCHAR(300),
  description text,
  path VARCHAR(300),
  insert_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  size INTEGER,
  PRIMARY KEY (attachment_id)  
);

CREATE TABLE news_attachment_item (
  item_id INTEGER NOT NULL,
  attachment_id  INTEGER NOT NULL,
  PRIMARY KEY (item_id, attachment_id),
  CONSTRAINT news_fk FOREIGN KEY (item_id) REFERENCES news_item (item_id),
  CONSTRAINT attachment_fk FOREIGN KEY (attachment_id) REFERENCES news_attachment (attachment_id) 
);

CREATE TABLE news_attachment_options (
  attachment_options_id INTEGER NOT NULL,
  is_app_options CHAR(1) NOT NULL default '0',
  max_size INTEGER,
  authorized_files_extensions VARCHAR(300),
  forbidden_files_extensions VARCHAR(300),
  PRIMARY KEY (attachment_options_id)  
);

CREATE TABLE news_entity_attachment_options (
  attachment_options_id BIGINT NOT NULL,
  entity_id  BIGINT NOT NULL,
  PRIMARY KEY (attachment_options_id, entity_id),
  CONSTRAINT options_fk1 FOREIGN KEY (attachment_options_id) REFERENCES news_attachment_options (attachment_options_id),
  CONSTRAINT options_fk2 FOREIGN KEY (entity_id) REFERENCES news_entity (entity_id) 
);

CREATE TABLE news_cmis_server (
  server_id  INTEGER NOT NULL,
  is_default_server CHAR(1) NOT NULL default '0',
  server_login VARCHAR(100),
  server_pwd VARCHAR(100),
  server_url VARCHAR(200),
  repository_id VARCHAR(100),
  PRIMARY KEY (server_id)  
);

CREATE TABLE news_entity_cmis_server (
  server_id INTEGER NOT NULL,
  entity_id INTEGER NOT NULL,
  PRIMARY KEY (server_id, entity_id),
  CONSTRAINT entity_cmis_server_fk1 FOREIGN KEY (server_id) REFERENCES news_cmis_server (server_id),
  CONSTRAINT entity_cmis_server_fk2 FOREIGN KEY (entity_id) REFERENCES news_entity (entity_id) 
);

INSERT INTO news_sequence VALUES ('attachment', 1);
INSERT INTO news_sequence VALUES ('attachment_options', 1);
INSERT INTO news_sequence VALUES ('cmis_server', 1);