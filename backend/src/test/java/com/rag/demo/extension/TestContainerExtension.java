package com.rag.demo.extension;

import com.rag.demo.configuration.ChatModelConfig;
import com.rag.demo.configuration.EmbeddingModelConfig;
import com.rag.demo.extension.db.DatabaseTestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
@ActiveProfiles("test")
@Import({
        EmbeddingModelConfig.class,
        ChatModelConfig.class
})
public abstract class TestContainerExtension {

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer = DatabaseTestInstance.getInstance();

    public EntityManager getEm() {
        return em;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @BeforeAll
    public static void beforeAll() {
        postgresContainer.start();
    }

}
