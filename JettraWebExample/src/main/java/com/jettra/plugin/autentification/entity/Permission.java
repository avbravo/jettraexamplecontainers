package com.jettra.plugin.autentification.entity;

import java.util.UUID;

public record Permission(
    UUID id,
    String name,
    String description,
    String resourcePath 
) {}
