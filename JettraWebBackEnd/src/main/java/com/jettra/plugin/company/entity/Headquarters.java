/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jettra.plugin.company.entity;

import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import java.util.UUID;

/**
 *
 * @author avbravo
 */
public record Headquarters(
        @NotNull
        @Size(min = 3)
        UUID id,
        @NotNull
        @Size(min = 3)
        String name,
        @NotNull
        Company company,
        @NotNull
        Boolean active) {

}
