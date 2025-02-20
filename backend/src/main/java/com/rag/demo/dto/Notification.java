package com.rag.demo.dto;


public record Notification(String message, boolean error, boolean finished, String topic) {
}
