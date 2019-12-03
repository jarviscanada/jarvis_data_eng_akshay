DROP DATABASE IF EXISTS host_agent;
CREATE DATABASE host_agent;

\c host_agent;

CREATE TABLE PUBLIC.host_info 
  ( 
     id               SERIAL NOT NULL, 
     hostname         VARCHAR NOT NULL UNIQUE, 
     cpu_number		  SMALLINT NOT NULL,
	 cpu_architecture VARCHAR NOT NULL,
	 cpu_model 		  VARCHAR NOT NULL,
	 cpu_mhz  		  INT NOT NULL,
	 L2_cache	      INT NOT NULL,
	 total_mem		  INT NOT NULL,
	 "timestamp" 	  VARCHAR NOT NULL,
	 CONSTRAINT pk_host_info PRIMARY KEY  (id),
	 CONSTRAINT unique_host_name UNIQUE (hostname)
  );


CREATE TABLE PUBLIC.host_usage 
  ( 
     "timestamp"    TIMESTAMP NOT NULL, 
     host_id        SERIAL NOT NULL, 
     memory_free 	INT NOT NULL,
	 cpu_idle 		SMALLINT NOT NULL,
	 cpu_kernel 	SMALLINT NOT NULL,
	 disk_io 		SMALLINT NOT NULL,
	 disk_available INT NOT NULL,
	 CONSTRAINT host_id_host_name_fk FOREIGN KEY (host_id) REFERENCES 
     host_info(id)
  ); 

