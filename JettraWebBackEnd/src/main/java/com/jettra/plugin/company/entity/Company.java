/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.jettra.plugin.company.entity;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import java.util.UUID;

/**
 *
 * @author avbravo
 */
public record Company(
        @NotNull
        @Size(min = 3)
        UUID id,
        @NotNull
        String name,
        @NotNull
        Boolean active
        ) {

}
