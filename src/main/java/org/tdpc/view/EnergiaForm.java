package org.tdpc.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.tdpc.model.Energia;
import org.tdpc.service.ConfigService;

public class EnergiaForm {

    // Método único para abrir o formulário
    public static void abrir(TableView<Energia> tabela) {

        Stage janela = new Stage();
        janela.setTitle("Configuração de Energia");

        // --- Campos de entrada ---
        TextField potenciaField = new TextField();
        potenciaField.setPromptText("Potência (W)");

        TextField precoField = new TextField();
        precoField.setPromptText("Preço R$/kWh");

        Button salvarBtn = new Button("Salvar");

        // --- Ação do botão salvar ---
        salvarBtn.setOnAction(e -> {
            try {
                double potencia = Double.parseDouble(potenciaField.getText());
                double preco = Double.parseDouble(precoField.getText());

                Energia en = new Energia();
                en.id = ConfigService.gerarId();
                en.potencia = potencia;
                en.precoKwh = preco;

                // adiciona ao config e salva
                ConfigService.config.energias.add(en);
                ConfigService.salvar();

                // atualiza tabela imediatamente
                if (tabela != null) {
                    tabela.getItems().add(en);
                }

                janela.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Digite números válidos para potência e preço.");
                alert.showAndWait();
            }
        });

        // --- Layout ---
        VBox layout = new VBox(10,
                new Label("Potência (W):"), potenciaField,
                new Label("Preço (R$/kWh):"), precoField,
                salvarBtn
        );
        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 200));
        janela.show();
    }

    public static void abrirEdicao(TableView<Energia> tabela, Energia existente) {
        Stage janela = new Stage();
        janela.setTitle("Editar Energia");

        TextField potenciaField = new TextField(String.valueOf(existente.potencia));
        TextField precoField = new TextField(String.valueOf(existente.precoKwh));

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            try {
                double novaPotencia = Double.parseDouble(potenciaField.getText());
                double novoPreco = Double.parseDouble(precoField.getText());

                // atualiza objeto existente
                existente.potencia = novaPotencia;
                existente.precoKwh = novoPreco;

                ConfigService.salvar();

                // força atualização da tabela
                tabela.refresh();

                janela.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Digite números válidos para potência e preço.");
                alert.setHeaderText("Entrada inválida");
                alert.showAndWait();
            }
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