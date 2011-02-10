  
--
-- Structure for table jasper_portlet
--
DROP TABLE IF EXISTS jasper_portlet;
CREATE TABLE jasper_portlet (
  id_portlet int default '0' NOT NULL,
  jasper_feed_id varchar(100) default NULL,
  PRIMARY KEY  (id_portlet)
);
