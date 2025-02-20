package com.rag.demo.resource;

import com.rag.demo.domain.ChatHistory;
import com.rag.demo.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-history")
public class ChatHistoryResource {

    private final ChatHistoryRepository chatHistoryRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ChatHistory> historical() {
        return chatHistoryRepository.findAll();
    }

}
