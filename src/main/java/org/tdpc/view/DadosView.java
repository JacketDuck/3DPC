package org.tdpc.view;

import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.tdpc.factory.TabelaFactory;
import org.tdpc.model.Energia;
import org.tdpc.model.Filamento;
import org.tdpc.model.Tempo;

public class DadosView {

    // Variáveis estáticas para acesso global às tabelas
    public static TableView<Filamento> tabelaFilamentos;
    public static TableView<Energia> tabelaEnergia;
    public static TableView<Tempo> tabelaTempo;

    public static TabPane criar() {

        // --- Criação das tabelas tipadas ---
        tabelaFilamentos = TabelaFactory.criarTabelaFilamentos();
        StackPane filamentosPane = TabelaFactory.aplicarLogo(tabelaFilamentos);

        tabelaEnergia = TabelaFactory.criarTabelaEnergia();
        StackPane energiaPane = TabelaFactory.aplicarLogo(tabelaEnergia);

        tabelaTempo = TabelaFactory.criarTabelaTempo();
        StackPane tempoPane = TabelaFactory.aplicarLogo(tabelaTempo);

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
