package org.tdpc;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FilamentoForm {

    public static void abrir(TableView<Filamento> tabela) {

        Stage janela = new Stage();
        janela.setTitle("Configuração de Filamento");

        // --- Campos ---
        TextField tipoField = new TextField();
        tipoField.setPromptText("PLA, ABS...");

        TextField precoField = new TextField();
        precoField.setPromptText("Preço do rolo (R$)");

        TextField pesoField = new TextField();
        pesoField.setPromptText("Peso do rolo (kg)");

        Button salvarBtn = new Button("Salvar");

        // --- Ação do botão salvar ---
        salvarBtn.setOnAction(e -> {
            try {
                String tipo = tipoField.getText().trim();

                // valida se o campo está vazio
                if (tipo.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Erro: O campo 'Tipo' não pode estar vazio.");
                    alert.setHeaderText("Entrada inválida");
                    alert.showAndWait();
                    return;
                }

                // valida se já existe filamento com o mesmo nome
                boolean existe = ConfigService.config.filamentos.stream()
                        .anyMatch(f -> f.tipo.equalsIgnoreCase(tipo));
                if (existe) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Já existe um filamento com o tipo '" + tipo + "'.");
                    alert.setHeaderText("Duplicado");
                    alert.showAndWait();
                    return;
                }

                // tenta converter os campos numéricos
                double preco = Double.parseDouble(precoField.getText());
                double peso = Double.parseDouble(pesoField.getText());

                // cria o filamento
                Filamento f = new Filamento();
                f.id = ConfigService.gerarId();
                f.tipo = tipo;
                f.precoRolo = preco;
                f.pesoRolo = peso;

                // salva no config
                ConfigService.config.filamentos.add(f);
                ConfigService.salvar();

                // adiciona na tabela imediatamente
                if (tabela != null) {
                    tabela.getItems().add(f);
                }

                janela.close();
            } catch (NumberFormatException ex) {
                // alerta o usuário
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Erro: Preço e Peso devem ser números válidos.");
                alert.setHeaderText("Entrada inválida");
                alert.showAndWait();
            }
        });

        // --- Layout ---
        VBox layout = new VBox(10,
                new Label("Tipo:"), tipoField,
                new Label("Preço (R$):"), precoField,
                new Label("Peso (kg):"), pesoField,
                salvarBtn
        );
        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 250));
        janela.show();
    }

    public static void abrirEdicao(TableView<Filamento> tabela, Filamento existente) {
        Stage janela = new Stage();
        janela.setTitle("Editar Filamento");

        TextField tipoField = new TextField(existente.tipo);
        TextField precoField = new TextField(String.valueOf(existente.precoRolo));
        TextField pesoField = new TextField(String.valueOf(existente.pesoRolo));

        Button salvarBtn = new Button("Salvar");

        salvarBtn.setOnAction(e -> {
            try {
                String novoTipo = tipoField.getText().trim();

                // valida duplicado (exceto se for o mesmo objeto)
                boolean existe = ConfigService.config.filamentos.stream()
                        .anyMatch(f -> f != existente && f.tipo.equalsIgnoreCase(novoTipo));
                if (existe) {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Já existe um filamento com o tipo '" + novoTipo + "'.");
                    alert.setHeaderText("Duplicado");
                    alert.showAndWait();
                    return;
                }

                double preco = Double.parseDouble(precoField.getText());
                double peso = Double.parseDouble(pesoField.getText());

                // atualiza objeto existente
                existente.tipo = novoTipo;
                existente.precoRolo = preco;
                existente.pesoRolo = peso;

                ConfigService.salvar();

                // força atualização da tabela
                tabela.refresh();

                janela.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Erro: Preço e Peso devem ser números válidos.");
                alert.setHeaderText("Entrada inválida");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10,
                new Label("Tipo:"), tipoField,
                new Label("Preço (R$):"), precoField,
                new Label("Peso (kg):"), pesoField,
                salvarBtn
        );
        layout.setPadding(new Insets(15));

        janela.setScene(new Scene(layout, 300, 250));
        janela.show();
    }

}
