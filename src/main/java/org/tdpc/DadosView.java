package org.tdpc;

import javafx.scene.control.*;

public class DadosView {

    public static TabPane criar() {

        TabPane tabPane = new TabPane();

        Tab filamento = new Tab("Filamentos", TabelaFactory.filamentos());
        Tab energia = new Tab("Energia", TabelaFactory.energia());
        Tab tempo = new Tab("Tempo", TabelaFactory.tempo());

        filamento.setClosable(false);
        energia.setClosable(false);
        tempo.setClosable(false);

        tabPane.getTabs().addAll(filamento, energia, tempo);

        return tabPane;
    }
}