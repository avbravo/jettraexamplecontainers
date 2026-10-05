/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jettra.plugin.acreditation.entity;

import java.util.UUID;

/**
 *
 * @author avbravo
 */
public record Permission(
        UUID id,
        Role role,
        Feature feature,
        Boolean reader,
        Boolean write,
        Boolean delete,
        Boolean report
        ) {

}
