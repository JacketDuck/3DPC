package org.tdpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDateTime;

public class ConfigService {

    private static final String FILE_PATH = "config.json";

    // Gson com JSON bonito
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Config config = new Config();

    public static void salvar() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(config, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void carregar() {
        try (Reader reader = new FileReader(FILE_PATH)) {

            Config loaded = gson.fromJson(reader, Config.class);

            if (loaded != null) {
                config = loaded;
            }

        } catch (Exception e) {
            config = new Config();
        }
    }

    public static String gerarId() {
        return LocalDateTime.now().toString();
    }
}