package org.tdpc;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EnergiaForm {

    public static void abrir() {

        Stage janela = new Stage();
        janela.setTitle("Configuração de energia");

        TextField potenciaField = new TextField();
        TextField precoField = new TextField();

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            double potencia = Double.parseDouble(potenciaField.getText());
            double preco = Double.parseDouble(precoField.getText());

            Energia en = new Energia();
            en.id = ConfigService.gerarId();
            en.potencia = potencia;
            en.precoKwh = preco;

            ConfigService.config.energias.add(en);
            ConfigService.salvar();

            janela.close();
        });

        VBox layout = new VBox(10,
                new Label("Potência (W):"), potenciaField,
                new Label("Preço (R$/kWh):"), precoField,
                salvarBtn
        );

        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 200));
        janela.show();
    }
}