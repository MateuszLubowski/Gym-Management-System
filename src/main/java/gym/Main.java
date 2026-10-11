package gym;

import gym.view.RezerwacjaController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        RezerwacjaController controller = new RezerwacjaController(stage);
        controller.pokazEkran1();

        stage.setWidth(600);
        stage.setHeight(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}