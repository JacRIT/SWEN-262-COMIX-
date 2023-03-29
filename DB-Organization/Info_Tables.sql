CREATE TABLE IF NOT EXISTS publisher_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    p_name          VARCHAR NOT NULL                                     --DD                           
);

CREATE TABLE IF NOT EXISTS creator_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    c_name          VARCHAR NOT NULL                                     --DD
);

CREATE TABLE IF NOT EXISTS character_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    character_name  VARCHAR NOT NULL                                     --DD
);

CREATE TABLE IF NOT EXISTS comic_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    series          VARCHAR NOT NULL                                    ,--DD
    title           VARCHAR NOT NULL                                    ,
    volume_num      INTEGER NOT NULL                                    ,
    issue_num       VARCHAR NOT NULL                                    ,

    initial_value   FLOAT                                               ,
    descrip         VARCHAR                                             ,

    release_date    VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    collection_fk   INTEGER NOT NULL                                    ,--FK

    last_name       VARCHAR NOT NULL    DEFAULT 'Doe'                   ,--DD
    first_name      VARCHAR NOT NULL    DEFAULT 'John'                  ,
    username        VARCHAR(12) NOT NULL                                
);

CREATE TABLE IF NOT EXISTS collection_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    nickname        VARCHAR                                              --DD
);

CREATE TABLE IF NOT EXISTS signature_info(
    id              SERIAL PRIMARY KEY                                  ,--PK

    s_name          VARCHAR                                             ,--DD
    authenticated   BOOLEAN
);