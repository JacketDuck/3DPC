package org.tdpc;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FilamentoForm {

    public static void abrir() {

        Stage janela = new Stage();
        janela.setTitle("Configuração de filamento");

        TextField tipoField = new TextField();
        tipoField.setPromptText("PLA, ABS...");

        TextField precoField = new TextField();
        precoField.setPromptText("Preço do rolo (R$)");

        TextField pesoField = new TextField();
        pesoField.setPromptText("Peso do rolo (kg)");

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            String tipo = tipoField.getText();
            double preco = Double.parseDouble(precoField.getText());
            double peso = Double.parseDouble(pesoField.getText());

            Filamento f = new Filamento();
            f.id = ConfigService.gerarId();
            f.tipo = tipo;
            f.precoRolo = preco;
            f.pesoRolo = peso;

            ConfigService.config.filamentos.add(f);
            ConfigService.salvar();

            janela.close();
        });

        VBox layout = new VBox(10,
                new Label("Tipo:"), tipoField,
                new Label("Preço:"), precoField,
                new Label("Peso:"), pesoField,
                salvarBtn
        );

        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 250));
        janela.show();
    }
}