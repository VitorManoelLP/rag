package com.rag.demo.configuration;

import org.mockito.Mockito;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class ChatModelConfig {

    @Bean
    public ChatModel chatModel() {

        final ChatModel mock = mock(ChatModel.class);

        final ChatResponse response = mock(ChatResponse.class);

        final Generation generation = mock(Generation.class);

        final AssistantMessage assistantMessage = mock(AssistantMessage.class);

        when(assistantMessage.getText()).thenReturn("Answer");

        when(generation.getOutput()).thenReturn(assistantMessage);

        when(response.getResult()).thenReturn(generation);

        when(mock.call(any(Prompt.class))).thenReturn(response);

        return mock;
    }

}
