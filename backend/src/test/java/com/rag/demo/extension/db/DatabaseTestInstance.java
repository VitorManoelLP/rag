package com.rag.demo.extension.db;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class DatabaseTestInstance extends PostgreSQLContainer<DatabaseTestInstance> {

    private static DatabaseTestInstance container;

    private DatabaseTestInstance() {
        super(DockerImageName.parse("pgvector/pgvector:pg16"));
    }

    public static DatabaseTestInstance getInstance() {
        if (container == null) {
            container = init();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    private static DatabaseTestInstance init() {
        return new DatabaseTestInstance()
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("postgres")
                .withEnv(Map.of(
                        "PGDATA", "/var/lib/postgresql/data"
                ))
                .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"))
                .withReuse(true);
    }

}
