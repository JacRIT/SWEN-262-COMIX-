CREATE TABLE publisher_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    p_name          VARCHAR NOT NULL                                     --DD                           
);

CREATE TABLE creator_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    c_name          VARCHAR NOT NULL                                     --DD
);

CREATE TABLE character_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    character_name  VARCHAR NOT NULL                                     --DD
);

CREATE TABLE comic_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    series          VARCHAR NOT NULL                                    ,--DD
    title           VARCHAR NOT NULL                                    ,
    volume_num      INTEGER NOT NULL                                    ,
    issue_num       VARCHAR NOT NULL                                    ,

    initial_value   FLOAT                                               ,
    descrip         VARCHAR                                             ,

    release_date    VARCHAR NOT NULL
);

CREATE TABLE user_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    last_name       VARCHAR NOT NULL    DEFAULT 'Doe'                   ,--DD
    first_name      VARCHAR NOT NULL    DEFAULT 'John'                  ,
    username        VARCHAR(12) NOT NULL                                
);

CREATE TABLE collection_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    nickname        VARCHAR                                              --DD
);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO USER 'swen262' ;