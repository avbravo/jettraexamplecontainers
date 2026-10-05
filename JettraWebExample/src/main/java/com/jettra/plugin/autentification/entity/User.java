package com.jettra.plugin.autentification.entity;

import java.util.UUID;
import java.util.Set;

public record User(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phone,
    Boolean active,
    Department department,
    Set<Role> roles
) {}
