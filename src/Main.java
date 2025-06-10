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
            new Ant(21,15),
            new Ant(20,16),
            new Ant(19,15),
            new Ant(21,15)
        };

        Timeline tick = new Timeline(
            new KeyFrame(Duration.millis(400), e -> {
                for (Ant ant : ants) {
                    int rand = (int)(Math.random() * 4); // 0 to 3
                    int oldX = ant.getPosX();
                    int oldY = ant.getPosY();
                    int step = 1 + (int)(Math.random() * 2); // 1 or 2

                    int dirX = 0, dirY = 0;

                    switch (rand) {
                        case 0: dirX = 1; break; // Right
                        case 1: dirX = -1; break; // Left
                        case 2: dirY = 1; break; // Down
                        case 3: dirY = -1; break; // Up
                    }

                    boolean pathClear = true;
                    int tempX = oldX, tempY = oldY;

                    for (int i = 0; i < step; i++) {
                        tempX += dirX;
                        tempY += dirY;

                        if (
                            !matrix.inBounds(tempX, tempY) ||
                            matrix.isObstacle(tempX, tempY) ||
                            matrix.isColony(tempX, tempY)
                        ) {
                            pathClear = false;
                            break;
                        }
                    }

                    if (pathClear) {
                        ant.moveTo(tempX, tempY, Duration.millis(330));
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