
--
-- Structure for table jasper
--

DROP TABLE IF EXISTS jasper;

CREATE TABLE jasper (		
id_report int default '0' NOT NULL,
description varchar(50) default '' NOT NULL,
url varchar(255) default '' NOT NULL,
pool varchar(255) default '' NOT NULL,
file_folder varchar(255) default '' NOT NULL,
PRIMARY KEY  (id_report)
);

DROP TABLE IF EXISTS jasper_file_format;

CREATE TABLE jasper_file_format (
  id_report int default '0' NOT NULL,
  file_format varchar(50) default '' NOT NULL,
  PRIMARY KEY  (id_report)
) ;