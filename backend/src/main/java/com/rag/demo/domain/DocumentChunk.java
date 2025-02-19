package com.rag.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Getter
@Builder
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@Table(name = "document_chunk")
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnoreProperties("chunks")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_document")
    private Document document;

    private String content;

    @JdbcTypeCode(10000)
    @Column(columnDefinition = "vector(1536)")
    private float[] chunk;

}
