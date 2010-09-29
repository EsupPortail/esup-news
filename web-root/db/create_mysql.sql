CREATE TABLE news_entity (
  entity_id BIGINT(20) NOT NULL auto_increment,
  name VARCHAR(250) default '',
  description TEXT,
  created_by VARCHAR(50) default NULL,
  creation_date DATETIME,
  display_priority int(4) default '0',
  CONSTRAINT news_entity_pk   PRIMARY KEY (entity_id),
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


CREATE TABLE news_category (
  cat_id bigint(20) NOT NULL default '0',
  rss_allowed char(1) default '1',
  name varchar(200) NOT NULL default '',
  description text NOT NULL,
  created_by varchar(50) default NULL,
  creation_date datetime,
  last_update_date datetime,
  lang char(3) default NULL,
  refresh_period varchar(10) default NULL,
  refresh_frequency int(11) default NULL,
  display_order int(11) default '0',
  public_view char(1) default '1',
  entity_id BIGINT(20) NOT NULL default '0',
  CONSTRAINT news_cat_pk   PRIMARY KEY (cat_id),
  INDEX (entity_id),
  CONSTRAINT cat_entity_fk FOREIGN KEY (entity_id) REFERENCES news_entity(entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE news_item (
  item_id bigint(20) NOT NULL default '0',
  title varchar(250) NOT NULL default 'titre par défaut',
  summary text,
  body text,
  post_date datetime,
  start_date date,
  end_date date,
  last_update_date datetime,
  posted_by varchar(50) default NULL,
  last_updated_by varchar(50) default NULL,
  status char(1) default '0',
  category_id bigint(20) NOT NULL default '0',
  CONSTRAINT  news_item_pk   PRIMARY KEY (item_id ),
  CONSTRAINT  item_fk FOREIGN KEY (category_id) REFERENCES news_category(cat_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;




CREATE TABLE news_role (
  role_id int(11) NOT NULL,
  role_name varchar(25) NOT NULL default '',
  role_desc_key varchar(100) NOT NULL default '',
  CONSTRAINT  news_role_pk   PRIMARY KEY (role_name)   
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



INSERT INTO `news_role` (`role_id`, `role_name`, `role_desc_key`) VALUES 
(1, 'ROLE_USER', 'role.user.desc'),
(2, 'ROLE_CONTRIBUTOR', 'role.contributor.desc'),
(3, 'ROLE_EDITOR', 'role.editor.desc'),
(4, 'ROLE_MANAGER', 'role.manager.desc'),
(5, 'ROLE_ADMIN', 'role.superAdm.desc');


CREATE TABLE news_sequence (
  name varchar(30) NOT NULL default '',
  value bigint(20) NOT NULL default '0',
  CONSTRAINT  news_sequence_pk   PRIMARY KEY (name) 
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO `news_sequence` VALUES ('category', 1);
INSERT INTO `news_sequence` VALUES ('item', 1);
INSERT INTO `news_sequence` VALUES ('role', 10);
INSERT INTO `news_sequence` VALUES ('subscriber', 1);
INSERT INTO `news_sequence` VALUES ('topic', 1);
INSERT INTO `news_sequence` VALUES ('attachment', 1);
INSERT INTO `news_sequence` VALUES ('attachment_options', 1);
INSERT INTO `news_sequence` VALUES ('cmis_server', 1);

CREATE TABLE news_subscribe_type (
  sub_type varchar(30) NOT NULL default '',
  sub_desc varchar(100) NOT NULL default '',
  CONSTRAINT  news_subscribe_type_pk   PRIMARY KEY (sub_type)  
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


INSERT INTO news_subscribe_type (`sub_type`, `sub_desc`) VALUES 
('FORCED_SUB', 'Forced subscriber'),
('FREE_SUB', 'User can freely subscribe'),
('PRE_SUB', 'users are pre-subscribed');



CREATE TABLE news_subscribers (
  id bigint(20) NOT NULL default '0',
  ctx_id bigint(20)  NOT NULL default '0',
  ctx_type char(1) NOT NULL default '',
  sub_key varchar(150) NOT NULL default '',
  sub_is_group char(1) NOT NULL default '',
  sub_type varchar(30) NOT NULL default '',
  CONSTRAINT  news_subscribers_pk   PRIMARY KEY (id) 
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



CREATE TABLE news_topic (
  topic_id bigint(20) NOT NULL default '0',
  rss_allowed char(1) NOT NULL default '1',
  name varchar(200) NOT NULL,
  description text,
  lang char(3) default NULL,
  created_by varchar(50) default NULL,
  creation_date datetime,
  refresh_period varchar(10) default NULL,
  refresh_frequency int(11) default NULL,
  last_update_date datetime,
  category_id bigint(20) NOT NULL default '0',
  display_order bigint(20) NOT NULL default '0',
  public_view char(1) NOT NULL default '1',
  CONSTRAINT  news_topic_pk   PRIMARY KEY (topic_id),
  CONSTRAINT  topic_fk        FOREIGN KEY (category_id) REFERENCES news_category(cat_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



CREATE TABLE news_topic_item (
  topic_id bigint(20) NOT NULL default '0',
  item_id bigint(20) NOT NULL default '0',
  display_order bigint(20) default '0',
  CONSTRAINT  news_topic_item_pk   PRIMARY KEY (`topic_id`,`item_id`),
  CONSTRAINT  topic_item_fk1       FOREIGN KEY (topic_id) REFERENCES news_topic(topic_id),  
CONSTRAINT  topic_item_fk2       FOREIGN KEY (item_id) REFERENCES news_item(item_id) 
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



CREATE TABLE news_user (
  user_id varchar(50) NOT NULL default '',
  is_sup_adm char(1) NOT NULL default '0',
  enabled char(1) default NULL,
  register_date date,
  last_access datetime,
  CONSTRAINT  news_user_pk   PRIMARY KEY (user_id)  
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;



INSERT INTO news_user (`user_id`, `is_sup_adm`, `enabled`) VALUES 
('[SUPER_USER_UID]', '1',  '1');


CREATE TABLE news_user_role (
  principal varchar(150) NOT NULL default '',
  is_group char(1) NOT NULL default '0',
  role_name varchar(25) NOT NULL default '',
  ctx_id bigint(20) NOT NULL default '0',
  ctx_type char(1) NOT NULL default '',
  CONSTRAINT  news_user_role_PK   PRIMARY KEY  (`principal`,`is_group`,`ctx_id`,`ctx_type`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


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