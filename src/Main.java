import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage){
        
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        Matrix matrix = new Matrix();
        matrix.generateWorld("gh5w6j2bs2", 8,30);

        Ant ant0 = new Ant(4,5);

        matrix.displayWorld(root, ()->{
            ant0.displayAnt(root);
        });
        
        primaryStage.getIcons().add(new Image("file:img/ant-ico.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Calyx Point");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}