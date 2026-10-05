package com.jettra.main3.util;

import java.util.HashMap;
import java.util.Scanner;
import io.jettra.jwt.JettraJWT;

public class GenerateToken {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese username: ");
        String username = scanner.nextLine();
        
        System.out.print("Ingrese password: ");
        String password = scanner.nextLine();
        
        // Aquí podrías validar contra una base de datos o lógica real.
        // Para el ejemplo, generamos el JWT si envía información válida.
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Error: Credenciales inválidas.");
            System.exit(1);
        }

        String secret = "default_secret_key_jettra_rest_2026";
        long expiration = 3600000; // 1 hora

        JettraJWT jwt = new JettraJWT(secret, expiration);
        String token = jwt.generateToken(new HashMap<>(), username);

        System.out.println("\n[JWT Generado Exitosamente]");
        System.out.println("Bearer " + token);
    }
}
