package org.tdpc;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TempoForm {

    public static void abrir() {

        Stage janela = new Stage();
        janela.setTitle("Configuração de tempo");

        TextField tempoField = new TextField();

        ComboBox<String> box = new ComboBox<>();
        box.getItems().addAll("Simples", "Médio", "Complexo");
        box.setValue("Médio");

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            double tempo = Double.parseDouble(tempoField.getText());

            double fator = switch (box.getValue()) {
                case "Simples" -> 1.0;
                case "Médio" -> 1.2;
                case "Complexo" -> 1.5;
                default -> 1.0;
            };

            Tempo t = new Tempo();
            t.id = ConfigService.gerarId();
            t.tempoPorGrama = tempo;
            t.fator = fator;

            ConfigService.config.tempos.add(t);
            ConfigService.salvar();

            janela.close();
        });

        VBox layout = new VBox(10,
                new Label("Min/g:"), tempoField,
                new Label("Complexidade:"), box,
                salvarBtn
        );

        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 220));
        janela.show();
    }
}