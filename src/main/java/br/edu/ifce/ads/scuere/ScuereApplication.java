package br.edu.ifce.ads.scuere;

import br.edu.ifce.ads.scuere.database.ConexaoDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScuereApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ConexaoDB.criarTabelas();

        FXMLLoader fxmlLoader = new FXMLLoader(ScuereApplication.class.getResource("/br/edu/ifce/ads/scuere/scuere-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Scuere - Loja de Motos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}