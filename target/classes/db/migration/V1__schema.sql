CREATE TABLE IF NOT EXISTS yohana_printing.users (
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username 		VARCHAR(50)  						NOT NULL UNIQUE,
  password 		VARCHAR(255) 						NOT NULL,
  enabled  		BOOLEAN      						NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						DEFAULT 'Morissoft System',
  updated_by	VARCHAR(255)						DEFAULT 'Morissoft System'
)
  ENGINE = InnoDb;
CREATE TABLE IF NOT EXISTS yohana_printing.authorities (
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username  	VARCHAR(50) 						NOT NULL,
  authority 	VARCHAR(50) 						NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						DEFAULT 'Morissoft System',
  updated_by	VARCHAR(255)						DEFAULT 'Morissoft System',
  FOREIGN KEY (username) REFERENCES yohana_printing.users (username),
  UNIQUE INDEX authorities_idx_1 (username, authority)
)
  ENGINE = InnoDb;

CREATE TABLE IF NOT EXISTS yohana_printing.customers (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name  	VARCHAR(50)                         NOT NULL,
  last_name   	VARCHAR(255)                        NULL,
  email        	VARCHAR(255)                        NULL UNIQUE,
  phone_number 	VARCHAR(255)						NULL UNIQUE,
  address		VARCHAR(255)						NULL,
  status        VARCHAR(255)                        NOT NULL DEFAULT 'ACTIVE',
  type          VARCHAR(255)                        NOT NULL DEFAULT 'RETAIL',
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL
)
  ENGINE = InnoDb;

CREATE TABLE IF NOT EXISTS yohana_printing.categories (	
  id         	BIGINT(50)                          NOT NULL PRIMARY KEY,
  name   		VARCHAR(255)                        NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL
)
  ENGINE = InnoDb;


CREATE TABLE IF NOT EXISTS yohana_printing.items (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code  		VARCHAR(50)                         NOT NULL,
  name   		VARCHAR(255)                        NOT NULL,
  category_id   BIGINT(50)							NOT NULL,
  status        VARCHAR(255)                        NOT NULL DEFAULT 'ACTIVE',
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  FOREIGN KEY (category_id) REFERENCES yohana_printing.categories (id)
)
  ENGINE = InnoDb;

CREATE TABLE IF NOT EXISTS yohana_printing.items_prices (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  item_id  	    BIGINT(50)                          NOT NULL,
  type 			VARCHAR(255)						NOT NULL DEFAULT 'RETAIL',
  qty_from   	INT                                 NOT NULL DEFAULT 1,
  qty_to	   	INT                                 NOT NULL DEFAULT 1,
  price			DECIMAL(13,2)						NOT NULL DEFAULT 1,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  FOREIGN KEY (item_id) REFERENCES yohana_printing.items (id)
)
  ENGINE = InnoDb;

 CREATE TABLE IF NOT EXISTS yohana_printing.sales_order (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_number  VARCHAR(255)                        NOT NULL UNIQUE,
  customer_id	BIGINT(50)							NOT NULL,
  completed_date TIMESTAMP							NOT NULL,
  sub_total		DECIMAL(13,2)						NOT NULL DEFAULT 1,
  discount		DECIMAL(13,2)						NOT NULL DEFAULT 0,
  payment_net	DECIMAL(13,2)						NULL DEFAULT 0,
  total_paid	DECIMAL(13,2)						NULL DEFAULT 0,
  total_outstanding	DECIMAL(13,2)					NOT NULL DEFAULT 0,
  status        VARCHAR(255)                        NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES yohana_printing.customers (id)
)
  ENGINE = InnoDb;

 CREATE TABLE IF NOT EXISTS yohana_printing.sales_order_detail (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id  	BIGINT(50)                    	    NOT NULL,
  item_id		BIGINT(50)							NOT NULL,
  length		DOUBLE(8,2)							NOT NULL DEFAULT 0,
  width			DOUBLE(8,2)							NOT NULL DEFAULT 0,
  qty			INT									NOT NULL DEFAULT 1,
  type 			VARCHAR(255)						NOT NULL DEFAULT 'RETAIL',
  price			DECIMAL(13,2)						NOT NULL DEFAULT 0,
  sub_total		DECIMAL(13,2)						NOT NULL DEFAULT 0,
  line_disc		DECIMAL(13,2)						NULL,
  line_total	DECIMAL(13,2)						NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  INDEX (order_id),
  INDEX (item_id),
  FOREIGN KEY (order_id) REFERENCES yohana_printing.sales_order (id),
  FOREIGN KEY (item_id) REFERENCES yohana_printing.items (id)
)
  ENGINE = InnoDb;
  
 CREATE TABLE IF NOT EXISTS yohana_printing.sales_invoice (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id		BIGINT(50)							NOT NULL,
  order_number  VARCHAR(255)                        NOT NULL,
  completed_date TIMESTAMP							NOT NULL,
  sub_total		DECIMAL(13,2)						NOT NULL DEFAULT 1,
  discount		DECIMAL(13,2)						NOT NULL DEFAULT 0,
  payment_net	DECIMAL(13,2)						NOT NULL DEFAULT 0,
  status        VARCHAR(255)                        NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  INDEX (order_id),
  FOREIGN KEY (order_id) REFERENCES yohana_printing.sales_order (id)
)
  ENGINE = InnoDb;

 CREATE TABLE IF NOT EXISTS yohana_printing.sales_invoice_detail (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  invoice_id  	BIGINT(50)                    	    NOT NULL,
  item_id		BIGINT(50)							NOT NULL,
  qty			INT									NOT NULL DEFAULT 1,
  price			DECIMAL(13,2)						NOT NULL DEFAULT 0,
  sub_total		DECIMAL(13,2)						NOT NULL DEFAULT 0,
  line_disc		DECIMAL(13,2)						NULL,
  line_total	DECIMAL(13,2)						NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  INDEX (invoice_id),
  INDEX (item_id),
  FOREIGN KEY (invoice_id) REFERENCES yohana_printing.sales_invoice (id),
  FOREIGN KEY (item_id) REFERENCES yohana_printing.items (id)
)
  ENGINE = InnoDb;

 CREATE TABLE IF NOT EXISTS yohana_printing.payment (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  payment_number VARCHAR(255)                    NOT NULL UNIQUE,
  invoice_id	BIGINT(50)							NOT NULL,
  payment_amount	DECIMAL(13,2)					NOT NULL DEFAULT 0,
  payment_method	INT								NOT NULL,
  status        VARCHAR(255)                        NOT NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  INDEX (invoice_id),
  FOREIGN KEY (invoice_id) REFERENCES yohana_printing.sales_invoice (id)
)
  ENGINE = InnoDb;
 
 CREATE TABLE IF NOT EXISTS yohana_printing.employee (	
  id         	BIGINT(50)                          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name  		VARCHAR(50)                         NOT NULL,
  last_name   	VARCHAR(255)                        NOT NULL,
  email        	VARCHAR(255)                        NOT NULL UNIQUE,
  phone  	 	VARCHAR(255)						NOT NULL UNIQUE,
  address		VARCHAR(255)						NULL,
  user_id	    BIGINT(50)							NULL UNIQUE,
  active		BOOLEAN								NULL,
  created_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  updated_at 	TIMESTAMP 							DEFAULT CURRENT_TIMESTAMP,
  created_by	VARCHAR(255)						NOT NULL,
  updated_by	VARCHAR(255)						NOT NULL,
  FOREIGN KEY (user_id) REFERENCES yohana_printing.users (id)
)
  ENGINE = InnoDb;
 