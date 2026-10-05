package com.jettra.plugin.example.library.entity;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;

public record Author(@NotNull
        @Size(min = 2, max = 5)
        String id,
        @Size(min = 2, max = 5)
        String name,
        String country) {

}
