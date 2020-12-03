#!/bin/bash

CREATE TABLE clients
(
    clientId UUID,
    name TEXT
);

# I use the new suuport of UUID of PostgreSQL 13 :)
INSERT INTO clients
VALUES (gen_random_uuid(), 'Batman'),
(gen_random_uuid(), 'Robin');