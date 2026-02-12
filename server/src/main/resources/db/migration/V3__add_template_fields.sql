ALTER TABLE templates
    ADD COLUMN raw_template TEXT,
    ADD COLUMN placeholders JSONB;
