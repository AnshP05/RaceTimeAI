import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage stage) {
        Label label = new Label("✅ JavaFX is working!");
        Scene scene = new Scene(label, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Test Window");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
