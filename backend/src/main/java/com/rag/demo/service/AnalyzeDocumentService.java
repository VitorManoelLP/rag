package com.rag.demo.service;

import com.rag.demo.domain.ChatHistory;
import com.rag.demo.dto.Answer;
import com.rag.demo.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class AnalyzeDocumentService {

    private final ChatModel chatModel;
    private final ChatHistoryRepository chatHistoryRepository;
    private final VectorStore vectorStore;

    @SneakyThrows
    public Answer analyze(String question) {

        final BeanOutputConverter<Answer> converter = new BeanOutputConverter<>(Answer.class);

        final List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder().topK(5).build());

        if (Objects.isNull(documents)) {
            return new Answer("No documents found to analyze", List.of(), 0.0);
        }

        final PromptTemplate promptTemplate = new PromptTemplate(Files.readString(ResourceUtils.getFile("classpath:prompt.st").toPath()));

        final Prompt prompt = promptTemplate.create(Map.of("question", question, "format", converter.getFormat(), "documents", documents));

        final Answer answer = converter.convert(Objects.requireNonNull(chatModel.call(prompt).getResult().getOutput().getText()));

        chatHistoryRepository.save(ChatHistory.builder()
                .userMessage(question)
                .botMessage(answer)
                .build());

        return answer;
    }

}