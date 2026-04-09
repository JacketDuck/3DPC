package org.tdpc.factory;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.tdpc.model.Energia;
import org.tdpc.model.Filamento;
import org.tdpc.model.Tempo;
import org.tdpc.service.ConfigService;
import org.tdpc.view.EnergiaForm;
import org.tdpc.view.FilamentoForm;
import org.tdpc.view.TempoForm;

public class TabelaFactory {

    // --- Logo para usar como marca d’água ---
    private static final Image LOGO = new Image(TabelaFactory.class.getResourceAsStream("/images/logo.png"));

    // Helper para criar StackPane com logo atrás da TableView
    public static <T> StackPane aplicarLogo(TableView<T> table) {
        ImageView logoView = new ImageView(LOGO);
        logoView.setOpacity(0.08);
        logoView.setPreserveRatio(true);

        StackPane stack = new StackPane(logoView, table);
        StackPane.setAlignment(logoView, Pos.CENTER);

        logoView.setFitWidth(400);
        logoView.setFitHeight(400);

        stack.widthProperty().addListener((obs, oldVal, newVal) ->
                logoView.setFitWidth(Math.max(400, newVal.doubleValue() * 0.995)));
        stack.heightProperty().addListener((obs, oldVal, newVal) ->
                logoView.setFitHeight(Math.max(400, newVal.doubleValue() * 0.995)));

        table.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-table-cell-border-color: #cccccc;" +
                        "-fx-table-header-border-color: #cccccc;" +
                        "-fx-text-fill: black;"
        );

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

        table.getColumns().forEach(col -> col.setStyle("-fx-text-fill: black;"));

        return stack;
    }

    // --- Tabela de Filamentos ---
    public static TableView<Filamento> criarTabelaFilamentos() {
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

        TableColumn<Filamento, Void> colAcao = criarColunaAcoesFilamento();

        t.getColumns().addAll(colTipo, colPreco, colPeso, colAcao);
        t.getItems().addAll(ConfigService.config.filamentos);

        configurarTabela(t);
        return t;
    }

    // --- Tabela de Energia ---
    public static TableView<Energia> criarTabelaEnergia() {
        TableView<Energia> t = new TableView<>();

        TableColumn<Energia, String> colPot = new TableColumn<>("Potência (W)");
        colPot.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().potencia)));
        colPot.setMinWidth(120);

        TableColumn<Energia, String> colPreco = new TableColumn<>("R$/kWh");
        colPreco.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().precoKwh)));
        colPreco.setMinWidth(80);

        TableColumn<Energia, Void> colAcao = criarColunaAcoesEnergia();

        t.getColumns().addAll(colPot, colPreco, colAcao);
        t.getItems().addAll(ConfigService.config.energias);

        configurarTabela(t);
        return t;
    }

    // --- Tabela de Tempo ---
    public static TableView<Tempo> criarTabelaTempo() {
        TableView<Tempo> t = new TableView<>();

        TableColumn<Tempo, String> colTempo = new TableColumn<>("Min/g");
        colTempo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().tempoPorGrama)));
        colTempo.setMinWidth(80);

        TableColumn<Tempo, String> colFator = new TableColumn<>("Fator");
        colFator.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().fator)));
        colFator.setMinWidth(80);

        TableColumn<Tempo, Void> colAcao = criarColunaAcoesTempo();

        t.getColumns().addAll(colTempo, colFator, colAcao);
        t.getItems().addAll(ConfigService.config.tempos);

        configurarTabela(t);
        return t;
    }

    // --- Configuração padrão de tabela ---
    private static <T> void configurarTabela(TableView<T> t) {
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setFixedCellSize(28);
        t.prefHeightProperty().bind(Bindings.size(t.getItems()).multiply(t.getFixedCellSize()).add(30));
    }

    // --- Colunas de ação ---
    private static TableColumn<Filamento, Void> criarColunaAcoesFilamento() {
        TableColumn<Filamento, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnExcluir = new Button("", new FontIcon("fa-trash"));
            private final Button btnEditar = new Button("", new FontIcon("fa-pencil"));

            {
                ((FontIcon) btnExcluir.getGraphic()).setIconSize(20);
                ((FontIcon) btnExcluir.getGraphic()).setIconColor(javafx.scene.paint.Color.RED);
                btnExcluir.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                btnExcluir.setOnAction(e -> {
                    Filamento f = getTableView().getItems().get(getIndex());
                    ConfigService.config.filamentos.remove(f);
                    ConfigService.salvar();
                    getTableView().getItems().remove(f);
                });

                ((FontIcon) btnEditar.getGraphic()).setIconSize(20);
                ((FontIcon) btnEditar.getGraphic()).setIconColor(javafx.scene.paint.Color.DODGERBLUE);
                btnEditar.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnEditar, new Tooltip("Editar"));
                btnEditar.setOnAction(e -> {
                    Filamento f = getTableView().getItems().get(getIndex());
                    FilamentoForm.abrirEdicao(getTableView(), f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, btnExcluir, btnEditar) {{
                    setAlignment(Pos.CENTER);
                    setStyle("-fx-background-color: transparent;");
                }});
            }
        });
        return colAcao;
    }

    private static TableColumn<Energia, Void> criarColunaAcoesEnergia() {
        TableColumn<Energia, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnExcluir = new Button("", new FontIcon("fa-trash"));
            private final Button btnEditar = new Button("", new FontIcon("fa-pencil"));

            {
                ((FontIcon) btnExcluir.getGraphic()).setIconSize(16);
                ((FontIcon) btnExcluir.getGraphic()).setIconColor(javafx.scene.paint.Color.RED);
                btnExcluir.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                btnExcluir.setOnAction(e -> {
                    Energia en = getTableView().getItems().get(getIndex());
                    ConfigService.config.energias.remove(en);
                    ConfigService.salvar();
                    getTableView().getItems().remove(en);
                });

                ((FontIcon) btnEditar.getGraphic()).setIconSize(16);
                ((FontIcon) btnEditar.getGraphic()).setIconColor(javafx.scene.paint.Color.DODGERBLUE);
                btnEditar.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnEditar, new Tooltip("Editar"));
                btnEditar.setOnAction(e -> {
                    Energia en = getTableView().getItems().get(getIndex());
                    EnergiaForm.abrirEdicao(getTableView(), en);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, btnExcluir, btnEditar) {{
                    setAlignment(Pos.CENTER);
                    setStyle("-fx-background-color: transparent;");
                }});
            }
        });
        return colAcao;
    }

    private static TableColumn<Tempo, Void> criarColunaAcoesTempo() {
        TableColumn<Tempo, Void> colAcao = new TableColumn<>("");
        colAcao.setMinWidth(80);
        colAcao.setCellFactory(param -> new TableCell<>() {
            private final Button btnExcluir = new Button("", new FontIcon("fa-trash"));
            private final Button btnEditar = new Button("", new FontIcon("fa-pencil"));

            {
                ((FontIcon) btnExcluir.getGraphic()).setIconSize(16);
                ((FontIcon) btnExcluir.getGraphic()).setIconColor(javafx.scene.paint.Color.RED);
                btnExcluir.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnExcluir, new Tooltip("Excluir"));
                btnExcluir.setOnAction(e -> {
                    Tempo tItem = getTableView().getItems().get(getIndex());
                    ConfigService.config.tempos.remove(tItem);
                    ConfigService.salvar();
                    getTableView().getItems().remove(tItem);
                });

                ((FontIcon) btnEditar.getGraphic()).setIconSize(16);
                ((FontIcon) btnEditar.getGraphic()).setIconColor(javafx.scene.paint.Color.DODGERBLUE);
                btnEditar.setStyle("-fx-background-color: transparent;");
                Tooltip.install(btnEditar, new Tooltip("Editar"));
                btnEditar.setOnAction(e -> {
                    Tempo tItem = getTableView().getItems().get(getIndex());
                    TempoForm.abrirEdicao(getTableView(), tItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, btnExcluir, btnEditar) {{
                    setAlignment(Pos.CENTER);
                    setStyle("-fx-background-color: transparent;");
                }});
            }
        });
        return colAcao;
    }
}