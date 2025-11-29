package com.fastcar.fastcarlocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Spring Boot.
 * Elle sert de point d'entrée pour l'exécution du service backend.
 */
@SpringBootApplication
public class FastCarLocationApplication {

    public static void main(String[] args) {
        // La méthode run lance l'application Spring Boot
        SpringApplication.run(FastCarLocationApplication.class, args);
        System.out.println("------------------------------------------");
        System.out.println("  FASTCAR LOCATION SERVICE EST LANCE !");
        System.out.println("  URL: http://localhost:8080");
        System.out.println("------------------------------------------");
    }

}

