SET foreign_key_checks = 0;
ALTER TABLE news_user DROP COLUMN user_name;
ALTER TABLE news_user DROP COLUMN email;
ALTER TABLE news_item DROP COLUMN post_user_name;

CREATE TABLE news_entity (
  entity_id BIGINT(20) NOT NULL auto_increment,
  name VARCHAR(250) default '',
  description TEXT,
  created_by VARCHAR(50) default NULL,
  creation_date DATETIME,
  display_priority int(4) default '0',
  CONSTRAINT  news_entity_pk   PRIMARY KEY (entity_id),
  CONSTRAINT unique_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE news_type (
  type_id BIGINT(20) NOT NULL auto_increment,
  name VARCHAR(250),
  description TEXT,
  CONSTRAINT  news_type_pk   PRIMARY KEY (type_id),
  CONSTRAINT unique_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO `news_type` (`type_id`,`name`, `description`) VALUES
(1,'DEFAULT', 'Type de catégorie par défaut.');

CREATE TABLE news_filter (
  filter_id BIGINT(20) NOT NULL auto_increment,
  attribute VARCHAR(250) NOT NULL,
  operator VARCHAR(10) NOT NULL,
  value VARCHAR(250) NOT NULL default '',
  type VARCHAR(50) NOT NULL,
  entity_id BIGINT(20) NOT NULL,
  CONSTRAINT news_filter_pk PRIMARY KEY (filter_id),
  INDEX (entity_id),
  CONSTRAINT filter_entity_fk FOREIGN KEY (entity_id) REFERENCES news_entity(entity_id),
  CONSTRAINT unique_filter UNIQUE (attribute, operator, value, type, entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

ALTER TABLE news_category
  ADD COLUMN entity_id BIGINT(20) NOT NULL,
  ADD INDEX (entity_id),
  ADD CONSTRAINT  cat_entity_fk   FOREIGN KEY (entity_id) REFERENCES news_entity(entity_id);


CREATE TABLE news_entity_type (
  entity_id BIGINT(20) NOT NULL,
  type_id BIGINT(20) NOT NULL,
  CONSTRAINT  news_entity_type_PK PRIMARY KEY (entity_id, type_id),
  INDEX (entity_id),
  CONSTRAINT entity_type_fk1 FOREIGN KEY (entity_id) REFERENCES news_entity (entity_id),
  INDEX (type_id),
  CONSTRAINT entity_type_fk2 FOREIGN KEY (type_id) REFERENCES news_type (type_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_category_type (
  cat_id BIGINT(20) NOT NULL,
  type_id BIGINT(20) NOT NULL,
  CONSTRAINT  news_category_type_PK PRIMARY KEY (cat_id, type_id),
  INDEX (cat_id),
  CONSTRAINT news_category_type_fk1 FOREIGN KEY (cat_id) REFERENCES news_category (cat_id),
  INDEX (type_id),
  CONSTRAINT news_category_type_fk2 FOREIGN KEY (type_id) REFERENCES news_type (type_id)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE news_attachment (
  attachment_id  BIGINT(20) NOT NULL auto_increment,
  cmis_uid varchar(250) default '',
  file_name varchar(100) default '',
  title varchar(300),
  description text,
  path varchar(300),
  insert_date date,
  size BIGINT(20),
  CONSTRAINT  attachment_pk   PRIMARY KEY (attachment_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_attachment_item (
  item_id BIGINT(20) NOT NULL,
  attachment_id  BIGINT(20) NOT NULL,
  CONSTRAINT  news_attachment_PK PRIMARY KEY (item_id, attachment_id),
  INDEX (item_id),
  CONSTRAINT news_fk1 FOREIGN KEY (item_id) REFERENCES news_item (item_id),
  INDEX (attachment_id),
  CONSTRAINT attachment_fk2 FOREIGN KEY (attachment_id) REFERENCES news_attachment (attachment_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_attachment_options (
  attachment_options_id  BIGINT(20) NOT NULL,
  is_app_options char(1) NOT NULL default '0',
  max_size BIGINT(20),
  authorized_files_extensions varchar(300),
  forbidden_files_extensions varchar(300),
  CONSTRAINT  attachment_options_pk   PRIMARY KEY (attachment_options_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_entity_attachment_options (
  attachment_options_id BIGINT(20) NOT NULL,
  entity_id  BIGINT(20) NOT NULL,
  CONSTRAINT  news_entity_attachment_options_PK PRIMARY KEY (attachment_options_id, entity_id),
  INDEX (attachment_options_id),
  CONSTRAINT options_fk1 FOREIGN KEY (attachment_options_id) REFERENCES news_attachment_options (attachment_options_id),
  INDEX (entity_id),
  CONSTRAINT options_fk2 FOREIGN KEY (entity_id) REFERENCES news_entity (entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_cmis_server (
  server_id  BIGINT(20) NOT NULL,
  is_default_server char(1) NOT NULL default '0',
  server_login varchar(100),
  server_pwd varchar(100),
  server_url varchar(200),
  repository_id varchar(100),
  CONSTRAINT  cmis_server_pk   PRIMARY KEY (server_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE news_entity_cmis_server (
  server_id  BIGINT(20) NOT NULL,
  entity_id  BIGINT(20) NOT NULL,
  CONSTRAINT  entity_cmis_server_PK PRIMARY KEY (server_id, entity_id),
  INDEX (server_id),
  CONSTRAINT entity_cmis_server_fk1 FOREIGN KEY (server_id) REFERENCES news_cmis_server (server_id),
  INDEX (entity_id),
  CONSTRAINT entity_cmis_server_fk2 FOREIGN KEY (entity_id) REFERENCES news_entity (entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO `news_sequence` VALUES ('attachment', 1);
INSERT INTO `news_sequence` VALUES ('attachment_options', 1);
INSERT INTO `news_sequence` VALUES ('cmis_server', 1);
SET foreign_key_checks = 1;