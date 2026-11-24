
CREATE TABLE folders (
                         id UUID PRIMARY KEY,
                         owner_id UUID NOT NULL,
                         parent_folder_id UUID,
                         name VARCHAR(100) NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL,
                         updated_at TIMESTAMPTZ NOT NULL,

                         CONSTRAINT fk_folders_owner
                             FOREIGN KEY (owner_id)
                                 REFERENCES users(id)
                                 ON DELETE CASCADE,

                         CONSTRAINT fk_folders_parent
                             FOREIGN KEY (parent_folder_id)
                                 REFERENCES folders(id)
                                 ON DELETE RESTRICT,

                         CONSTRAINT chk_folders_name_not_blank
                             CHECK (length(trim(name)) > 0)
);

CREATE INDEX idx_folders_owner_parent
    ON folders(owner_id, parent_folder_id);

CREATE UNIQUE INDEX uk_folders_root_name
    ON folders(owner_id, lower(name))
    WHERE parent_folder_id IS NULL;

CREATE UNIQUE INDEX uk_folders_child_name
    ON folders(owner_id, parent_folder_id, lower(name))
    WHERE parent_folder_id IS NOT NULL;
