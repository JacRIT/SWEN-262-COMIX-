CREATE TABLE publisher_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    publisher_fk     INTEGER NOT NULL                                   ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE creator_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    creator_fk      INTEGER NOT NULL                                    ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE character_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    character_fk    INTEGER NOT NULL                                    ,--FK
    comic_fk        INTEGER NOT NULL                                    
);

CREATE TABLE comic_ownership(
    id              SERIAL PRIMARY KEY                                  ,--PK

    comic_fk        INTEGER NOT NULL                                    ,--FK

    comic_value     INTEGER                                             ,--DD
    grade           INTEGER                                             ,
    slabbed         BOOLEAN
);

CREATE TABLE collection_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    collection_fk   INTEGER NOT NULL                                    ,--FK
    copy_fk         INTEGER NOT NULL
);

CREATE TABLE collection_ownership(
    id              SERIAL PRIMARY KEY                                  ,--PK

    user_fk         INTEGER NOT NULL                                    ,--FK
    collection_fk   INTEGER NOT NULL                                    
);

CREATE TABLE subcollection_refrence(
    id              SERIAL PRIMARY KEY                                  ,--PK

    collect_fk      INTEGER NOT NULL                                    ,--FK
    subcollect_fk   INTEGER NOT NULL
);