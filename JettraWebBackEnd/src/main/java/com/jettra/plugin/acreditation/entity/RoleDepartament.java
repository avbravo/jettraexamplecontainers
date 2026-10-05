/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.jettra.plugin.acreditation.entity;

import com.jettra.plugin.company.entity.Department;
import io.jettra.rules.validations.NotNull;
import io.jettra.rules.validations.Size;
import java.util.UUID;

/**
 *
 * @author avbravo Description: Administra los roles por departamento por user.
 * Se usa para casos en los que las validaciones son necesarias para usuarios
 * con varios roles por departamento.
 * Es una clase embebida dentro de User
 */

public record RoleDepartament(
        @NotNull
        @Size(min = 3)
        UUID id,       
        @NotNull
        Role role,
        @NotNull
        Department department,
        @NotNull
        Boolean active
        ) {}
