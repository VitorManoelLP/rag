package com.rag.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatModelService {

    private final ChatModel chatModel;

    @SneakyThrows
    public String getAnswer(Map<String, Object> promptMetadata, String promptPath) {
        final PromptTemplate promptTemplate = new PromptTemplate(Files.readString(ResourceUtils.getFile(promptPath).toPath()));
        final Prompt prompt = promptTemplate.create(promptMetadata);
        final ChatResponse chatResponse = chatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

}
