--CREATE DATABASE esupnews WITH ENCODING 'UTF8';
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

CREATE TABLE news_category (
  cat_id INTEGER NOT NULL default 0,
  rss_allowed CHAR(1) default '1',
  name VARCHAR(200) NOT NULL default '',
  description TEXT NOT NULL,
  created_by VARCHAR(50) default NULL,
  creation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  lang CHAR(3) default NULL,
  refresh_period VARCHAR(10) default NULL,
  refresh_frequency INTEGER default NULL,
  display_order INTEGER default 0,
  public_view CHAR(1) default '1',
  entity_id BIGINT NOT NULL,
  cat_id_next BIGINT,
  cat_id_prev BIGINT,
  PRIMARY KEY(cat_id),
  CONSTRAINT cat_entity_fk FOREIGN KEY (entity_id)
                  REFERENCES news_entity (entity_id),
  CONSTRAINT cat_next_fk FOREIGN KEY (cat_id_next)
                  REFERENCES news_category (cat_id),
  CONSTRAINT cat_prev_fk FOREIGN KEY (cat_id_prev)
                  REFERENCES news_category (cat_id)
);
CREATE INDEX idx_cat_entity ON news_category(entity_id);



CREATE TABLE news_item (
  item_id INTEGER NOT NULL default 0,
  title VARCHAR(250) NOT NULL default '',
  summary TEXT,
  body TEXT,
  post_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATE,
  end_date DATE,
  last_update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  posted_by VARCHAR(50) default NULL,
  last_updated_by VARCHAR(50) default NULL,
  status CHAR(1) default '0',
  category_id INTEGER NOT NULL default 0,
  PRIMARY KEY(item_id)
);
CREATE INDEX idx_item_cat ON news_item(category_id);
CREATE INDEX idx_item_status ON news_item(status);



CREATE TABLE news_role (
  role_id INTEGER NOT NULL,
  role_name VARCHAR(25) NOT NULL default '',
  role_desc_key VARCHAR(100) NOT NULL default '',
  PRIMARY KEY(role_name)
);



INSERT INTO news_role (role_id, role_name, role_desc_key) VALUES
(1, 'ROLE_USER', 'role.user.desc'),
(2, 'ROLE_CONTRIBUTOR', 'role.contributor.desc'),
(3, 'ROLE_EDITOR', 'role.editor.desc'),
(4, 'ROLE_MANAGER', 'role.manager.desc'),
(5, 'ROLE_ADMIN', 'role.superAdm.desc');


CREATE TABLE news_sequence (
  name VARCHAR(30) NOT NULL default '',
  value INTEGER NOT NULL default 0,
  PRIMARY KEY(name)
);

INSERT INTO news_sequence VALUES ('category', 1);
INSERT INTO news_sequence VALUES ('item', 1);
INSERT INTO news_sequence VALUES ('role', 10);
INSERT INTO news_sequence VALUES ('subscriber', 1);
INSERT INTO news_sequence VALUES ('topic', 1);
INSERT INTO news_sequence VALUES ('attachment', 1);
INSERT INTO news_sequence VALUES ('attachment_options', 1);
INSERT INTO news_sequence VALUES ('cmis_server', 1);

CREATE TABLE news_subscribe_type (
  sub_type VARCHAR(30) NOT NULL default '',
  sub_desc VARCHAR(100) NOT NULL default '',
  PRIMARY KEY(sub_type)
);


INSERT INTO news_subscribe_type (sub_type, sub_desc) VALUES
('FORCED_SUB', 'Forced subscriber'),
('FREE_SUB', 'User can freely subscribe'),
('PRE_SUB', 'users are pre-subscribed');



CREATE TABLE news_subscribers (
  id INTEGER NOT NULL default 0,
  ctx_id INTEGER  NOT NULL default 0,
  ctx_type CHAR(1) NOT NULL default '',
  sub_key VARCHAR(250) NOT NULL default '',
  sub_is_group CHAR(1) NOT NULL default '',
  sub_type VARCHAR(30) NOT NULL default '',
  PRIMARY KEY(id)
) ;



CREATE TABLE news_topic (
  topic_id INTEGER NOT NULL default 0,
  rss_allowed CHAR(1) NOT NULL default '1',
  name VARCHAR(200) NOT NULL default '',
  description TEXT,
  lang CHAR(3) default NULL,
  created_by VARCHAR(50) default NULL,
  creation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  refresh_period VARCHAR(10) default NULL,
  refresh_frequency INTEGER default NULL,
  last_update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  category_id INTEGER NOT NULL default 0,
  display_order INTEGER NOT NULL default 1,
  public_view CHAR(1) NOT NULL default '1',
  PRIMARY KEY(topic_id)
);
CREATE INDEX idx_topic_cat ON news_topic(category_id);


CREATE TABLE news_topic_item (
  topic_id INTEGER NOT NULL default 0,
  item_id INTEGER NOT NULL default 0,
  display_order INTEGER default 1,
  PRIMARY KEY (topic_id, item_id)

);

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


CREATE TABLE news_user (
  user_id VARCHAR(50) NOT NULL default '',
  is_sup_adm CHAR(1) NOT NULL default '0',
  enabled CHAR(1) default NULL,
  register_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_access timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(user_id)
);



INSERT INTO news_user (user_id, user_name, email, is_sup_adm, enabled) VALUES
('[SUPER_USER_UID]', '[SUPER_USER_UID]', '[SUPER_USER_EMAIL]', '1',  '1');


CREATE TABLE news_user_role (
  principal VARCHAR(250) NOT NULL default '',
  is_group CHAR(1) NOT NULL default '0',
  role_name VARCHAR(25) NOT NULL default '',
  ctx_id INTEGER NOT NULL default 0,
  ctx_type CHAR(1) NOT NULL default ''
);
CREATE INDEX idx_user_role_p ON news_user_role(principal);
CREATE INDEX idx_user_role_g ON news_user_role(is_group);
CREATE INDEX idx_user_role_cid ON news_user_role(ctx_id);
CREATE INDEX idx_user_role_ct ON news_user_role(ctx_type);


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
