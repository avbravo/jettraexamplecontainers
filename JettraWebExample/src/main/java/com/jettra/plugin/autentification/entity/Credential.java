package com.jettra.plugin.autentification.entity;

import java.time.Instant;
import java.util.UUID;

public record Credential(
    UUID id,
    User user,
    String username,
    String passwordHash,
    Boolean active,
    Instant lastLogin
) {}
