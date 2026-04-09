package org.tdpc;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {

        ConfigService.carregar(); // carrega do JSON

        Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
        stage.getIcons().add(logo);

        // converter listas para ObservableList
        ConfigService.config.filamentos = FXCollections.observableArrayList(ConfigService.config.filamentos);
        ConfigService.config.energias = FXCollections.observableArrayList(ConfigService.config.energias);
        ConfigService.config.tempos = FXCollections.observableArrayList(ConfigService.config.tempos);

        root = new BorderPane();

        MenuBar menuBar = criarMenu();
        root.setTop(menuBar);

        TabPane mainTabs = new TabPane();

        Tab dashboardTab = new Tab("Dashboard", DashboardView.criar());
        dashboardTab.setClosable(false);

        Tab dadosTab = new Tab("Dados", DadosView.criar());
        dadosTab.setClosable(false);

        mainTabs.getTabs().addAll(dashboardTab, dadosTab);

        mainTabs.getSelectionModel().select(dashboardTab);

        root.setCenter(mainTabs);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("3D Cost Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar criarMenu() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem sair = new MenuItem("Sair");
        sair.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(sair);

        Menu configMenu = new Menu("Configurações");

        MenuItem filamento = new MenuItem("Filamento");
        filamento.setOnAction(e -> FilamentoForm.abrir(DadosView.tabelaFilamentos));

        MenuItem energia = new MenuItem("Energia");
        energia.setOnAction(e -> EnergiaForm.abrir(DadosView.tabelaEnergia));

        MenuItem tempo = new MenuItem("Tempo");
        tempo.setOnAction(e -> TempoForm.abrir(DadosView.tabelaTempo));

        configMenu.getItems().addAll(filamento, energia, tempo);

        menuBar.getMenus().addAll(fileMenu, configMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        launch();
    }
}
