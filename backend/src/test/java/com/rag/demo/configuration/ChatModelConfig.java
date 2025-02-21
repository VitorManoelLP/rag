package com.rag.demo.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public ChatClient client() {

        final ChatClient mock = mock(ChatClient.class);

        when(mock.prompt(any(String.class)).call().content()).thenReturn("{\"$schema\":\"https://json-schema.org/draft/2020-12/schema\",\"type\":\"object\",\"properties\":{\"usedDocuments\":[\"SÍNTESE NOVA FASE V. 24 N. 78 (1997): 383-410\",\"Ética a Nicômaco\",\"Dialnet-AEticaAristotelica-3713875.pdf\"],\"value\":\"Aristóteles afirma que a ética é uma disciplina que se ocupa das disposições da ação constitutivas para uma vida bem-sucedida, e utiliza a palavra grega 'ethos' para designar essa nova expressão ética. Ele define a ética como 'a filosofia das coisas humanas', diferenciando-a da filosofia teórica. A ética de Aristóteles é uma ética do bom senso, fundada nos juízos morais do homem bom e virtuoso, e articula-se a partir da pergunta fundamental: qual é o bem supremo do homem e o fim a que tendem todas as coisas? O bem supremo do homem é a felicidade, que é a atividade da alma conforme a razão e a virtude. Cada ação deve ter um fim último que tenha um valor nele mesmo, e a ética não é demonstrativa, sendo baseada nas opiniões do grande número de homens.\"},\"additionalProperties\":false}");

        return mock;
    }

}
