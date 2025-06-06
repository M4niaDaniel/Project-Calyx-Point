import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage){
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Calyx Point");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        
        Matrix matrix = new Matrix();
        matrix.generateWorld("829LS2");

        launch(args);
    }
}