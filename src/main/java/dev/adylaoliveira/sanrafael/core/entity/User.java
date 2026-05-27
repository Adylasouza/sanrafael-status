package dev.adylaoliveira.sanrafael.core.entity;

public record User(
        String username,
        String password,
        String role
) {}