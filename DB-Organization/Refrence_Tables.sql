CREATE TABLE IF NOT EXISTS publisher_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    publisher_fk     INTEGER NOT NULL                                   ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE IF NOT EXISTS creator_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    creator_fk      INTEGER NOT NULL                                    ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE IF NOT EXISTS character_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    character_fk    INTEGER NOT NULL                                    ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE IF NOT EXISTS signature_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    signature_fk    INTEGER NOT NULL                                    ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE IF NOT EXISTS comic_ownership(
    id              SERIAL PRIMARY KEY                                  ,--PK

    comic_fk        INTEGER NOT NULL                                    ,--FK

    comic_value     INTEGER                                             ,--DD
    grade           INTEGER                                             ,
    slabbed         BOOLEAN
);

CREATE TABLE IF NOT EXISTS collection_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    collection_fk   INTEGER NOT NULL                                    ,--FK
    copy_fk         INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS subcollection_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    collect_fk      INTEGER NOT NULL                                    ,--FK
    subcollect_fk   INTEGER NOT NULL
);