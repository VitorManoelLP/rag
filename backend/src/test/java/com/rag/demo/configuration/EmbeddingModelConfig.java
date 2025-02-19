package com.rag.demo.configuration;

import com.rag.demo.fixture.ChunkFixture;
import org.mockito.Mockito;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Configuration
public class EmbeddingModelConfig {

    @Bean
    public EmbeddingModel embeddingModel() {

        final EmbeddingModel mock = Mockito.mock(EmbeddingModel.class);

        when(mock.embed(any(String.class))).thenAnswer(invocation -> ChunkFixture.getBaseChunk());

        return mock;
    }

}
