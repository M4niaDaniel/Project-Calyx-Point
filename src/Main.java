import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private final List<Timeline> antTimelines = new ArrayList<>(); // to stop ants later

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        Matrix matrix = new Matrix();
        matrix.generateWorld("gh5w6j2bs2", 8, 30);

        Ant[] ants = new Ant[]{
            new Ant(20, 16, 0),
            new Ant(19, 15, 1),
            new Ant(21, 15, 2),
            new Ant(20, 16, 3),
            new Ant(19, 15, 4),
            new Ant(21, 15, 5)
        };

        matrix.displayWorld(root, () -> {
            for (Ant ant : ants) {
                ant.displayAnt(root);

                // Random interval between 300 and 800 ms
                int interval = 300 + (int) (Math.random() * 500);

                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(interval), e -> {
                    moveAntRandomly(ant, matrix);
                }));

                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

                antTimelines.add(timeline);
            }
        });

        primaryStage.getIcons().add(new Image("file:img/ant-ico.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Calyx Point");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void moveAntRandomly(Ant ant, Matrix matrix) {
        int rand = (int) (Math.random() * 4); // 0 to 3
        int oldX = ant.getPosX();
        int oldY = ant.getPosY();
        int step = 1 + (int) (Math.random() * 2); // 1 or 2

        int dirX = 0, dirY = 0;
        switch (rand) {
            case 0 -> dirX = 1;  // Right
            case 1 -> dirX = -1; // Left
            case 2 -> dirY = 1;  // Down
            case 3 -> dirY = -1; // Up
        }

        boolean pathClear = true;
        int tempX = oldX;
        int tempY = oldY;

        for (int i = 0; i < step; i++) {
            tempX += dirX;
            tempY += dirY;

            if(matrix.isFood(tempX, tempY)){
                stopAnt(ant.getId());
                matrix.setFood(tempX, tempY, false);
            }

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
            ant.moveTo(tempX, tempY, Duration.millis(300));
        }
    }
    public void stopAnt(int id) {
        if (id >= 0 && id < antTimelines.size()) {
            antTimelines.get(id).stop();
        }
    }
    public void resumeAnt(int id) {
        if (id >= 0 && id < antTimelines.size()) {
            antTimelines.get(id).play();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
