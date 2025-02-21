package com.rag.demo.repository;

import com.rag.demo.domain.ChatHistory;
import com.rag.demo.dto.HistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, UUID> {

    @Query(value =
            "SELECT dc.id as id, dc.bot_message as answer, (1 - (dc.embedding <=> cast(:embedding as vector(1536)))) as similarity " +
                    "FROM chat_history dc " +
                    "ORDER BY dc.embedding <=> cast(:embedding as vector(1536)) " +
                    "LIMIT 1",
            nativeQuery = true)
    Optional<HistoryProjection> findSimilarEmbedding(@Param("embedding") String embedding);

}
