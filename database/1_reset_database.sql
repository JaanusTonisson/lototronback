-- Kustutab public schema (mis põhimõtteliselt kustutab kõik tabelid)
DROP SCHEMA IF EXISTS lototron CASCADE;
-- Loob uue public schema vajalikud õigused
CREATE SCHEMA lototron
-- taastab vajalikud andmebaasi õigused
    GRANT ALL ON SCHEMA lototron TO postgres;
GRANT ALL ON SCHEMA lototron TO PUBLIC;