package org.tdpc.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import org.tdpc.model.Config;

import java.io.*;
import java.util.ArrayList;

public class ConfigService {

    private static final String FILE_PATH = "config.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
            config = (loaded != null) ? loaded : new Config();
        } catch (Exception e) {
            e.printStackTrace();
            config = new Config();
        }

        // Inicializa listas caso estejam nulas
        if (config.filamentos == null) config.filamentos = new ArrayList<>();
        if (config.energias == null) config.energias = new ArrayList<>();
        if (config.tempos == null) config.tempos = new ArrayList<>();

        // Converte **somente aqui** para ObservableList
        config.filamentos = FXCollections.observableArrayList(config.filamentos);
        config.energias = FXCollections.observableArrayList(config.energias);
        config.tempos = FXCollections.observableArrayList(config.tempos);
    }

    public static String gerarId() {
        return java.time.LocalDateTime.now().toString();
    }
}