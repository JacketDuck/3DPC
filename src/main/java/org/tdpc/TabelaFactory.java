package org.tdpc;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;

public class TabelaFactory {

    // --- Logo para usar como marca d’água ---
    private static final Image LOGO = new Image(TabelaFactory.class.getResourceAsStream("/images/logo.png"));

    // Helper para criar StackPane com logo atrás da TableView
    public static <T> StackPane aplicarLogo(TableView<T> table) {
        ImageView logoView = new ImageView(LOGO);
        logoView.setOpacity(0.08); // visível mas discreto
        logoView.setPreserveRatio(true);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(logoView, table);

        StackPane.setAlignment(logoView, Pos.CENTER);

        // Tamanho inicial mínimo do logo
        logoView.setFitWidth(400);
        logoView.setFitHeight(400);

        // Ajuste proporcional ao tamanho da aba (logo ocupa ~70%)
        stack.widthProperty().addListener((obs, oldVal, newVal) -> {
            logoView.setFitWidth(Math.max(400, newVal.doubleValue() * 0.995));
        });
        stack.heightProperty().addListener((obs, oldVal, newVal) -> {
            logoView.setFitHeight(Math.max(400, newVal.doubleValue() * 0.995));
        });

        // fundo transparente mas texto e linhas visíveis
        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-table-cell-border-color: #cccccc;" +
                        "-fx-table-header-border-color: #cccccc;" +
                        "-fx-text-fill: black;"
        );

        // Forçar cor do texto das células
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setStyle("-fx-text-fill: black; -fx-background-color: transparent; -fx-border-color: #cccccc;");
                } else {
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });

        // Forçar cor dos cabeçalhos
        table.getColumns().forEach(col -> col.setStyle("-fx-text-fill: black;"));

        return stack;
    }

    public static StackPane filamentos() {
        TableView<Filamento> t = new TableView<>();

        TableColumn<Filamento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tipo));
        colTipo.setMinWidth(120);

        TableColumn<Filamento, String> colPreco = new TableColumn<>("Preço (R$)");
        colPreco.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().precoRolo)));
        colPreco.setMinWidth(60);

        TableColumn<Filamento, String> colPeso = new TableColumn<>("Peso (kg)");
        colPeso.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().pesoRolo)));
        colPeso.setMinWidth(60);

        TableColumn<Filamento, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final FontIcon iconTrash = new FontIcon("fa-trash");
            private final FontIcon iconEdit = new FontIcon("fa-pencil");

            private final Button btnExcluir = new Button("", iconTrash);
            private final Button btnEditar = new Button("", iconEdit);

            {
                // definir tamanho e cor dos ícones
                iconTrash.setIconSize(20);
                iconTrash.setIconColor(javafx.scene.paint.Color.RED);

                iconEdit.setIconSize(20);
                iconEdit.setIconColor(javafx.scene.paint.Color.DODGERBLUE);

                // estilo dos botões (sem fundo)
                btnExcluir.setStyle("-fx-background-color: transparent;");
                btnEditar.setStyle("-fx-background-color: transparent;");

                // tooltips
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                Tooltip.install(btnEditar, new Tooltip("Editar"));

                // ação excluir
                btnExcluir.setOnAction(e -> {
                    Filamento f = getTableView().getItems().get(getIndex());
                    ConfigService.config.filamentos.remove(f);
                    ConfigService.salvar();
                    getTableView().getItems().remove(f);
                });

                // ação editar
                btnEditar.setOnAction(e -> {
                    Filamento f = getTableView().getItems().get(getIndex());
                    FilamentoForm.abrirEdicao(getTableView(), f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // usar HBox para centralizar os botões
                    HBox box = new HBox(5, btnExcluir, btnEditar);
                    box.setAlignment(Pos.CENTER);
                    box.setStyle("-fx-background-color: transparent;");
                    setGraphic(box);
                }
            }
        });

        t.getColumns().addAll(colTipo, colPreco, colPeso, colAcao);
        t.getItems().addAll(ConfigService.config.filamentos);

        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setFixedCellSize(28);
        t.prefHeightProperty().bind(Bindings.size(t.getItems()).multiply(t.getFixedCellSize()).add(30));

        return aplicarLogo(t);
    }

    public static StackPane energia() {
        TableView<Energia> t = new TableView<>();

        TableColumn<Energia, String> colPot = new TableColumn<>("Potência (W)");
        colPot.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().potencia)));
        colPot.setMinWidth(120);

        TableColumn<Energia, String> colPreco = new TableColumn<>("R$/kWh");
        colPreco.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().precoKwh)));
        colPreco.setMinWidth(80);

        TableColumn<Energia, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final FontIcon iconTrash = new FontIcon("fa-trash");
            private final FontIcon iconEdit = new FontIcon("fa-pencil");

            private final Button btnExcluir = new Button("", iconTrash);
            private final Button btnEditar = new Button("", iconEdit);

            {
                // definir tamanho e cor dos ícones
                iconTrash.setIconSize(16);
                iconTrash.setIconColor(javafx.scene.paint.Color.RED);

                iconEdit.setIconSize(16);
                iconEdit.setIconColor(javafx.scene.paint.Color.DODGERBLUE);

                // estilo dos botões (sem fundo)
                btnExcluir.setStyle("-fx-background-color: transparent;");
                btnEditar.setStyle("-fx-background-color: transparent;");

                // tooltips
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                Tooltip.install(btnEditar, new Tooltip("Editar"));

                // ação excluir
                btnExcluir.setOnAction(e -> {
                    Energia en = getTableView().getItems().get(getIndex());
                    ConfigService.config.energias.remove(en);
                    ConfigService.salvar();
                    getTableView().getItems().remove(en);
                });

                // ação editar
                btnEditar.setOnAction(e -> {
                    Energia en = getTableView().getItems().get(getIndex());
                    EnergiaForm.abrirEdicao(getTableView(), en);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnExcluir, btnEditar);
                    box.setAlignment(Pos.CENTER);
                    box.setStyle("-fx-background-color: transparent;");
                    setGraphic(box);
                }
            }
        });

        t.getColumns().addAll(colPot, colPreco, colAcao);
        t.getItems().addAll(ConfigService.config.energias);

        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setFixedCellSize(28);
        t.prefHeightProperty().bind(Bindings.size(t.getItems()).multiply(t.getFixedCellSize()).add(30));

        return aplicarLogo(t);
    }

    public static StackPane tempo() {
        TableView<Tempo> t = new TableView<>();

        TableColumn<Tempo, String> colTempo = new TableColumn<>("Min/g");
        colTempo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().tempoPorGrama)));
        colTempo.setMinWidth(80);

        TableColumn<Tempo, String> colFator = new TableColumn<>("Fator");
        colFator.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().fator)));
        colFator.setMinWidth(80);

        TableColumn<Tempo, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final FontIcon iconTrash = new FontIcon("fa-trash");
            private final FontIcon iconEdit = new FontIcon("fa-pencil");

            private final Button btnExcluir = new Button("", iconTrash);
            private final Button btnEditar = new Button("", iconEdit);

            {
                // definir tamanho e cor dos ícones
                iconTrash.setIconSize(16);
                iconTrash.setIconColor(javafx.scene.paint.Color.RED);

                iconEdit.setIconSize(16);
                iconEdit.setIconColor(javafx.scene.paint.Color.DODGERBLUE);

                // estilo dos botões (sem fundo)
                btnExcluir.setStyle("-fx-background-color: transparent;");
                btnEditar.setStyle("-fx-background-color: transparent;");

                // tooltips
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                Tooltip.install(btnEditar, new Tooltip("Editar"));

                // ação excluir
                btnExcluir.setOnAction(e -> {
                    Tempo tItem = getTableView().getItems().get(getIndex());
                    ConfigService.config.tempos.remove(tItem);
                    ConfigService.salvar();
                    getTableView().getItems().remove(tItem);
                });

                // ação editar
                btnEditar.setOnAction(e -> {
                    Tempo tItem = getTableView().getItems().get(getIndex());
                    TempoForm.abrirEdicao(getTableView(), tItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnExcluir, btnEditar);
                    box.setAlignment(Pos.CENTER);
                    box.setStyle("-fx-background-color: transparent;");
                    setGraphic(box);
                }
            }
        });

        t.getColumns().addAll(colTempo, colFator, colAcao);
        t.getItems().addAll(ConfigService.config.tempos);

        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setFixedCellSize(28);
        t.prefHeightProperty().bind(Bindings.size(t.getItems()).multiply(t.getFixedCellSize()).add(30));

        return aplicarLogo(t);
    }
}
