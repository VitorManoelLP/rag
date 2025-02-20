package com.rag.demo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rag.demo.dto.Answer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;

import java.util.Objects;

@Converter
public class AnswerConverter implements AttributeConverter<Answer, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(Answer answer) {
        return objectMapper.writeValueAsString(answer);
    }

    @Override
    @SneakyThrows
    public Answer convertToEntityAttribute(String json) {
        if (Objects.isNull(json)) {
            return null;
        }
        return objectMapper.readValue(json, Answer.class);
    }
}
