package org.tdpc.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.tdpc.model.Tempo;
import org.tdpc.service.ConfigService;

public class TempoForm {

    public static void abrir(TableView<Tempo> tabela) {

        Stage janela = new Stage();
        janela.setTitle("Configuração de Tempo");

        // --- Campos de entrada ---
        TextField tempoField = new TextField();
        tempoField.setPromptText("Tempo por grama (min)");

        ComboBox<String> complexidadeBox = new ComboBox<>();
        complexidadeBox.getItems().addAll("Simples", "Médio", "Complexo");
        complexidadeBox.setValue("Médio"); // padrão

        Button salvarBtn = new Button("Salvar");

        // --- Ação do botão salvar ---
        salvarBtn.setOnAction(e -> {
            try {
                double tempoPorGrama = Double.parseDouble(tempoField.getText());

                Tempo t = new Tempo();
                t.id = ConfigService.gerarId();
                t.tempoPorGrama = tempoPorGrama;

                // Define o fator com base na complexidade
                switch (complexidadeBox.getValue()) {
                    case "Simples" -> t.fator = 0.8;
                    case "Médio" -> t.fator = 1.0;
                    case "Complexo" -> t.fator = 1.2;
                    default -> t.fator = 1.0;
                }

                // adiciona ao config e salva
                ConfigService.config.tempos.add(t);
                ConfigService.salvar();

                // atualiza tabela imediatamente
                if (tabela != null) {
                    tabela.getItems().add(t);
                }

                janela.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Digite um número válido para o tempo por grama.");
                alert.showAndWait();
            }
        });

        // --- Layout ---
        VBox layout = new VBox(10,
                new Label("Min/g:"), tempoField,
                new Label("Complexidade:"), complexidadeBox,
                salvarBtn
        );
        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 220));
        janela.show();
    }

    public static void abrirEdicao(TableView<Tempo> tabela, Tempo existente) {
        Stage janela = new Stage();
        janela.setTitle("Editar Tempo");

        TextField tempoField = new TextField(String.valueOf(existente.tempoPorGrama));

        ComboBox<String> complexidadeBox = new ComboBox<>();
        complexidadeBox.getItems().addAll("Simples", "Médio", "Complexo");

        // definir valor atual com base no fator
        if (existente.fator == 0.8) complexidadeBox.setValue("Simples");
        else if (existente.fator == 1.2) complexidadeBox.setValue("Complexo");
        else complexidadeBox.setValue("Médio");

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            try {
                double novoTempo = Double.parseDouble(tempoField.getText());

                existente.tempoPorGrama = novoTempo;

                // atualizar fator conforme complexidade escolhida
                switch (complexidadeBox.getValue()) {
                    case "Simples" -> existente.fator = 0.8;
                    case "Médio" -> existente.fator = 1.0;
                    case "Complexo" -> existente.fator = 1.2;
                    default -> existente.fator = 1.0;
                }

                ConfigService.salvar();

                // força atualização da tabela
                tabela.refresh();

                janela.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Digite um número válido para o tempo por grama.");
                alert.setHeaderText("Entrada inválida");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10,
                new Label("Min/g:"), tempoField,
                new Label("Complexidade:"), complexidadeBox,
                salvarBtn
        );
        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 220));
        janela.show();
    }

}