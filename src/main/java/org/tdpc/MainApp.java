package org.tdpc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {

        ConfigService.carregar();

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

        MenuItem novoCalculo = new MenuItem("Novo cálculo");

        MenuItem sair = new MenuItem("Sair");
        sair.setOnAction(e -> System.exit(0));

        Menu configMenu = new Menu("Configurações");

        MenuItem filamento = new MenuItem("Filamento");
        filamento.setOnAction(e -> FilamentoForm.abrir());

        MenuItem energia = new MenuItem("Energia");
        energia.setOnAction(e -> EnergiaForm.abrir());

        MenuItem tempo = new MenuItem("Tempo");
        tempo.setOnAction(e -> TempoForm.abrir());

        configMenu.getItems().addAll(filamento, energia, tempo);

        menuBar.getMenus().addAll(fileMenu, configMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        launch();
    }
}