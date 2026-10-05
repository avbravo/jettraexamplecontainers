package com.jettra.plugin.autentification.entity;

import java.util.UUID;

public record Department(
    UUID id,
    String name,
    UUID parentDepartmentId
) {}
