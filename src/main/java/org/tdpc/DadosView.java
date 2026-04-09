package org.tdpc;

import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class DadosView {

    // Variáveis estáticas para acesso global às tabelas
    public static TableView<Filamento> tabelaFilamentos;
    public static TableView<Energia> tabelaEnergia;
    public static TableView<Tempo> tabelaTempo;

    public static TabPane criar() {

        // --- Tabelas com marca d’água ---
        StackPane filamentosPane = TabelaFactory.filamentos();
        tabelaFilamentos = (TableView<Filamento>) filamentosPane.getChildren().get(1);

        StackPane energiaPane = TabelaFactory.energia();
        tabelaEnergia = (TableView<Energia>) energiaPane.getChildren().get(1);

        StackPane tempoPane = TabelaFactory.tempo();
        tabelaTempo = (TableView<Tempo>) tempoPane.getChildren().get(1);

        // --- Abas ---
        Tab filamentoTab = new Tab("Filamentos", filamentosPane);
        Tab energiaTab = new Tab("Energia", energiaPane);
        Tab tempoTab = new Tab("Tempo", tempoPane);

        filamentoTab.setClosable(false);
        energiaTab.setClosable(false);
        tempoTab.setClosable(false);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(filamentoTab, energiaTab, tempoTab);

        return tabPane;
    }
}
