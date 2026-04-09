package org.tdpc;

import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

public class TabelaFactory {

    public static TableView<Filamento> filamentos() {
        TableView<Filamento> t = new TableView<>();

        TableColumn<Filamento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().tipo)
        );

        TableColumn<Filamento, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().precoRolo))
        );

        TableColumn<Filamento, String> colPeso = new TableColumn<>("Peso");
        colPeso.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().pesoRolo))
        );

        // 🗑️ Coluna de excluir
        TableColumn<Filamento, Void> colAcao = new TableColumn<>("");

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("X");

            {
                btn.setOnAction(e -> {
                    Filamento f = getTableView().getItems().get(getIndex());

                    // remove do JSON
                    ConfigService.config.filamentos.remove(f);
                    ConfigService.salvar();

                    // remove da tabela
                    getTableView().getItems().remove(f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        t.getColumns().addAll(colTipo, colPreco, colPeso, colAcao);
        t.getItems().addAll(ConfigService.config.filamentos);

        return t;
    }

    public static TableView<Energia> energia() {
        TableView<Energia> t = new TableView<>();

        TableColumn<Energia, String> colPot = new TableColumn<>("Potência (W)");
        colPot.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().potencia))
        );

        TableColumn<Energia, String> colPreco = new TableColumn<>("R$/kWh");
        colPreco.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().precoKwh))
        );

        TableColumn<Energia, Void> colAcao = new TableColumn<>("");

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("X");

            {
                btn.setOnAction(e -> {
                    Energia en = getTableView().getItems().get(getIndex());

                    ConfigService.config.energias.remove(en);
                    ConfigService.salvar();

                    getTableView().getItems().remove(en);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        t.getColumns().addAll(colPot, colPreco, colAcao);

        t.getItems().addAll(ConfigService.config.energias);

        return t;
    }

    public static TableView<Tempo> tempo() {
        TableView<Tempo> t = new TableView<>();

        TableColumn<Tempo, String> colTempo = new TableColumn<>("Min/g");
        colTempo.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().tempoPorGrama))
        );

        TableColumn<Tempo, String> colFator = new TableColumn<>("Fator");
        colFator.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().fator))
        );

        TableColumn<Tempo, Void> colAcao = new TableColumn<>("");

        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("X");

            {
                btn.setOnAction(e -> {
                    Tempo t = getTableView().getItems().get(getIndex());

                    ConfigService.config.tempos.remove(t);
                    ConfigService.salvar();

                    getTableView().getItems().remove(t);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        t.getColumns().addAll(colTempo, colFator, colAcao);

        t.getItems().addAll(ConfigService.config.tempos);

        return t;
    }
}