package com.jettra.plugin.acreditation.entity;

import com.jettra.plugin.company.entity.Department;
import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import java.util.UUID;
import java.util.Set;

public record User(
        @NotNull
        @Size(min = 3)
        UUID id,
        @NotNull
        String username,
        @NotNull
        String passwordHash,
        @NotNull
        String identificationNumber,
        @NotNull
        @Size(min = 3)
        String firstName,
        @NotNull
        @Size(min = 3)
        String lastName,
        String email,
        String phone,
        @NotNull
        Boolean active,
        String photo,
        Set<RoleDepartament> roleDepartaments

        ) {

}
