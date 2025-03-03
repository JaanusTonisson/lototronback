-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-03-03 11:58:12.657

-- tables
-- Table: dining_service
CREATE TABLE dining_service (
                                id serial  NOT NULL,
                                name varchar(255)  NOT NULL,
                                CONSTRAINT dining_service_pk PRIMARY KEY (id)
);

-- Table: icebreaker_image
CREATE TABLE icebreaker_image (
                                  id serial  NOT NULL,
                                  data bytea  NOT NULL,
                                  icebreaker_question_id int  NOT NULL,
                                  CONSTRAINT icebreaker_image_pk PRIMARY KEY (id)
);

-- Table: icebreaker_question
CREATE TABLE icebreaker_question (
                                     id serial  NOT NULL,
                                     question varchar(255)  NOT NULL,
                                     CONSTRAINT icebreaker_question_pk PRIMARY KEY (id)
);

-- Table: lunch_event
CREATE TABLE lunch_event (
                             id serial  NOT NULL,
                             user_id int  NOT NULL,
                             restaurant_id int  NOT NULL,
                             pax_total int  NOT NULL,
                             pax_availalble int  NOT NULL,
                             date date  NOT NULL,
                             time time  NOT NULL,
                             status varchar(1)  NOT NULL,
                             is_available boolean  NOT NULL,
                             CONSTRAINT lunch_event_pk PRIMARY KEY (id)
);

-- Table: message
CREATE TABLE message (
                         id serial  NOT NULL,
                         receiver_user_id int  NOT NULL,
                         sender_user_id int  NOT NULL,
                         subject varchar(255)  NOT NULL,
                         body varchar(1000)  NOT NULL,
                         sender_type varchar(1)  NOT NULL,
                         state varchar(1)  NOT NULL,
                         CONSTRAINT message_pk PRIMARY KEY (id)
);

-- Table: profile
CREATE TABLE profile (
                         id serial  NOT NULL,
                         user_id int  NOT NULL,
                         phone_number varchar(255)  NOT NULL,
                         first_name varchar(255)  NOT NULL,
                         last_name varchar(255)  NOT NULL,
                         CONSTRAINT profile_pk PRIMARY KEY (id)
);

-- Table: register
CREATE TABLE register (
                          id serial  NOT NULL,
                          lunch_event_id int  NOT NULL,
                          user_id int  NOT NULL,
                          status int  NOT NULL,
                          CONSTRAINT register_pk PRIMARY KEY (id)
);

-- Table: restaurant
CREATE TABLE restaurant (
                            id serial  NOT NULL,
                            name varchar(255)  NOT NULL,
                            address varchar(255)  NOT NULL,
                            CONSTRAINT restaurant_pk PRIMARY KEY (id)
);

-- Table: restaurant_dining_service
CREATE TABLE restaurant_dining_service (
                                           id int  NOT NULL,
                                           dining_service_id int  NOT NULL,
                                           restaurant_id int  NOT NULL,
                                           CONSTRAINT restaurant_dining_service_pk PRIMARY KEY (id)
);

-- Table: restaurant_image
CREATE TABLE restaurant_image (
                                  id serial  NOT NULL,
                                  restaurant_id int  NOT NULL,
                                  data bytea  NOT NULL,
                                  CONSTRAINT restaurant_image_pk PRIMARY KEY (id)
);

-- Table: role
CREATE TABLE role (
                      id serial  NOT NULL,
                      name varchar(255)  NOT NULL,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE "user" (
                        id serial  NOT NULL,
                        role_id int  NOT NULL,
                        username varchar(255)  NOT NULL,
                        password varchar(255)  NOT NULL,
                        status varchar(1)  NOT NULL,
                        CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Table: user_image
CREATE TABLE user_image (
                            id serial  NOT NULL,
                            user_id int  NOT NULL,
                            data bytea  NOT NULL,
                            CONSTRAINT user_image_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: icebreaker_image_icebreaker_question (table: icebreaker_image)
ALTER TABLE icebreaker_image ADD CONSTRAINT icebreaker_image_icebreaker_question
    FOREIGN KEY (icebreaker_question_id)
        REFERENCES icebreaker_question (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: lunch_event_restaurant (table: lunch_event)
ALTER TABLE lunch_event ADD CONSTRAINT lunch_event_restaurant
    FOREIGN KEY (restaurant_id)
        REFERENCES restaurant (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: lunch_event_user (table: lunch_event)
ALTER TABLE lunch_event ADD CONSTRAINT lunch_event_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: profile_user (table: profile)
ALTER TABLE profile ADD CONSTRAINT profile_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: receiver_user (table: message)
ALTER TABLE message ADD CONSTRAINT receiver_user
    FOREIGN KEY (receiver_user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: register_lunch_event (table: register)
ALTER TABLE register ADD CONSTRAINT register_lunch_event
    FOREIGN KEY (lunch_event_id)
        REFERENCES lunch_event (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: register_user (table: register)
ALTER TABLE register ADD CONSTRAINT register_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: restaurant_dining_service_dining_service (table: restaurant_dining_service)
ALTER TABLE restaurant_dining_service ADD CONSTRAINT restaurant_dining_service_dining_service
    FOREIGN KEY (dining_service_id)
        REFERENCES dining_service (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: restaurant_image_restaurant (table: restaurant_image)
ALTER TABLE restaurant_image ADD CONSTRAINT restaurant_image_restaurant
    FOREIGN KEY (restaurant_id)
        REFERENCES restaurant (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: restaurant_service_restaurant (table: restaurant_dining_service)
ALTER TABLE restaurant_dining_service ADD CONSTRAINT restaurant_service_restaurant
    FOREIGN KEY (restaurant_id)
        REFERENCES restaurant (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: sender_user (table: message)
ALTER TABLE message ADD CONSTRAINT sender_user
    FOREIGN KEY (sender_user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_image_user (table: user_image)
ALTER TABLE user_image ADD CONSTRAINT user_image_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_role (table: user)
ALTER TABLE "user" ADD CONSTRAINT user_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- End of file.

