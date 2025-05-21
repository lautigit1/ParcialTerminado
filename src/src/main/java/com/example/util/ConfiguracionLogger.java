package com.example.util;

import java.io.IOException;
import java.util.logging.*;

public class ConfiguracionLogger {
    public static void configurar() {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);
        try {
            FileHandler fh = new FileHandler("registro.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
