-- =============================================================
-- Migration: overall-bugfix
-- Adds the `is_posted` column to the `shifts` table.
-- Run this script on the database BEFORE deploying the server.
-- =============================================================

ALTER TABLE shifts
    ADD COLUMN is_posted BOOLEAN NOT NULL DEFAULT FALSE;

-- Verify the result:
-- DESCRIBE shifts;
