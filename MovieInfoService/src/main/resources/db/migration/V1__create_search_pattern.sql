CREATE TABLE search_pattern (
                                   id serial primary key ,
                                   created datetime NOT NULL,
                                   title varchar(255) NOT NULL,
                                   api_name varchar(50) DEFAULT NULL,
                                   page_number int DEFAULT NULL
);