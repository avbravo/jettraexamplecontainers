package com.jettra.plugin.acreditation.entity;

import io.jettra.plugin.accreditation.enumerations.TypeFeature;
import java.util.UUID;

public record Feature(
        UUID id,
        String name,
        String description,
        String resourcePath,
        TypeFeature typeFeature
        ) {

}
