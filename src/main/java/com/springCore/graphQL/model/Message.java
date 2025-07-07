package com.springCore.graphQL.model;

import java.time.LocalDateTime;

public record Message(String id, String Content, LocalDateTime createdAt) {
}
