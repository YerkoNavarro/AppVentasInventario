package com.sistema.puntoventas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class PruebaPrediccionStock {

    public static void main(String[] args) {
        System.out.println("Iniciando prueba de prediccion_stock...");

        Path proyectoRaiz = Paths.get("").toAbsolutePath().normalize();
        Path script = proyectoRaiz.resolve("src/main/java/com/sistema/puntoventas/prediccion_stock.py");
        Path csv = proyectoRaiz.resolve("datos_stock.csv");

        if (!Files.exists(script)) {
            System.out.println("No se encontró el script: " + script);
            return;
        }

        if (!Files.exists(csv)) {
            System.out.println("No se encontró el CSV requerido: " + csv);
            return;
        }

        ProcessBuilder processBuilder = new ProcessBuilder("python", script.toString());
        processBuilder.directory(proyectoRaiz.toFile());
        processBuilder.redirectErrorStream(true);

        try {
            Process proceso = processBuilder.start();
            String salida;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                salida = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }

            int codigo = proceso.waitFor();
            System.out.println("--- Salida de la IA ---");
            if (!salida.isBlank()) {
                System.out.println(salida);
            }
            System.out.println("--------------------------------");
            System.out.println("Código de salida: " + codigo);
            System.out.println(codigo == 0 ? "Prueba finalizada correctamente." : "La prueba falló.");
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error al ejecutar la prueba: " + e.getMessage());
        }
    }
}

