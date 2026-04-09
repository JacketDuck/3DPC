package org.tdpc;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DashboardView {

    public static VBox criar() {

        HBox cards = new HBox(20);
        cards.setPadding(new Insets(15));

        VBox card1 = criarCard("Último custo", "R$ 0,00", "#4CAF50");
        VBox card2 = criarCard("Modelos analisados", "0", "#2196F3");

        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);

        cards.getChildren().addAll(card1, card2);

        TableView<String> tabela = new TableView<>();
        tabela.getItems().addAll("modelo1.stl", "modelo2.stl");

        VBox layout = new VBox(10, cards, tabela);
        layout.setPadding(new Insets(10));

        return layout;
    }

    private static VBox criarCard(String titulo, String valor, String cor) {
        Label t = new Label(titulo);
        t.setStyle("-fx-text-fill: white;");

        Label v = new Label(valor);
        v.setStyle("-fx-text-fill: white; -fx-font-size: 20;");

        VBox box = new VBox(5, t, v);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color:" + cor + "; -fx-background-radius:10;");
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
    }
}