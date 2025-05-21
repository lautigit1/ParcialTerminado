package com.example.util;

import java.io.IOException;
import java.util.logging.*;

public class ConfiguracionLogger {

    public static void configurar() {
        Logger logger = Logger.getLogger(""); // Logger raíz
        logger.setLevel(Level.INFO); // Nivel mínimo a registrar

        try {
            FileHandler fileHandler = new FileHandler("registro.log", true); // 'true' para modo append
            fileHandler.setFormatter(new SimpleFormatter()); // Formato de texto simple
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
