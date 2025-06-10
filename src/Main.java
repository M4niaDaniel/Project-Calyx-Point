import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
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

        Ant[] ants = new Ant[]{
            new Ant(20,16),
            new Ant(19,15),
            new Ant(21,15)
        };

        Timeline tick = new Timeline(
            new KeyFrame(Duration.millis(500), e -> {
                for (Ant ant : ants) {
                    int rand = (int)(Math.random() * 4); // 0 to 3
                    int newX = ant.getPosX();
                    int newY = ant.getPosY();

                    switch (rand) {
                        case 0: newX+=2; break; // Right
                        case 1: newX-=2; break; // Left
                        case 2: newY+=2; break; // Down
                        case 3: newY-=2; break; // Up
                    }

                    // Optional bounds check
                    if (newX >= 0 && newX < Matrix.WIDTH/Matrix.CELL_SIZE && newY >= 0 && newY < Matrix.HEIGHT/Matrix.CELL_SIZE) {
                        ant.moveTo(newX, newY, Duration.millis(400));
                    }
                }
            })
        );

        tick.setCycleCount(Timeline.INDEFINITE);
        

        matrix.displayWorld(root, ()->{
            for (Ant ant : ants) {
                ant.displayAnt(root);
            }
            tick.play();
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