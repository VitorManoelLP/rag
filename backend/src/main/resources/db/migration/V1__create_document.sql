CREATE
EXTENSION IF NOT EXISTS vector;

CREATE TABLE document
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255),
    hash VARCHAR(255)
);

CREATE TABLE document_chunk
(
    id          UUID PRIMARY KEY,
    id_document UUID REFERENCES document (id),
    chunk       vector(1536)
);

CREATE INDEX document_chunk_vector_idx ON document_chunk
    USING ivfflat (chunk vector_l2_ops);