package com.rag.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@ToString(of = "id")
@EqualsAndHashCode(of = "id")
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String hash;

    private long size;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
