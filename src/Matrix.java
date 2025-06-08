import javafx.util.Duration;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Matrix{
    private Cell[][] gridCells;
    int cols, rows;
    
    public static final int CELL_SIZE = 20;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    public Matrix(){        
        cols = WIDTH / CELL_SIZE;
        rows = HEIGHT / CELL_SIZE;

        gridCells = new Cell[rows][cols];
        init();
    }
    private boolean freeCell(int row, int column){
        if(!gridCells[row][column].isColony() && !gridCells[row][column].hasFood() && !gridCells[row][column].isObstacle()){
            return true;
        }else{return false;}
    }
    private void init(){

        for(int r=0; r<rows; r++){
            for(int c=0; c<cols; c++){
                gridCells[r][c] = new Cell(false, false, false);
            }
        }

        //Colony
        gridCells[rows/2][cols/2].setColony(true);
        System.out.println("Colony placed at: (" + rows/2 + ", " + cols/2 + ")");
    }
    public void generateWorld(String seed, int rocks, int food){
        //Seed based world generation
        long seedS = seed.hashCode();
        Random rand = new Random(seedS);

        int placedR = 0;

        int[][][] patterns = {
            {{0, 0}, {0, 1},{1, 0}, {1, 1}, {1, 2},{2, 1}, {2, 2}},
            {{0, 0}, {1, 0}, {1, 1}, {2, 0}, {2, 1}, {2, 2}},
            {{0, 0}, {0, 1}, {1, 1}, {2, 1}, {2, 2}},
            {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 0}}
        };
    
        while (placedR < rocks) {
            int index = rand.nextInt(patterns.length);
            int[][] pattern = patterns[index];

            int startRow = rand.nextInt(rows - 3);
            int startCol = rand.nextInt(cols - 3);

            boolean canPlace = true;

            for (int[] offset : pattern) {
                int row = startRow + offset[0];
                int col = startCol + offset[1];
                if (row >= rows || col >= cols || gridCells[row][col].isColony() || gridCells[row][col].isObstacle()) {
                    canPlace = false;
                    break;
                }
            }

            if (canPlace) {
                for (int[] offset : pattern) {
                    int row = startRow + offset[0];
                    int col = startCol + offset[1];
                    gridCells[row][col].setObstacle(true);
                    System.out.println("Rock placed at: " + row + " " + col);
                }
                placedR++;
            }
        }


        int placedF = 0;
        while (placedF <= food) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
    
            if (this.freeCell(row, col)){
                gridCells[row][col].setFood(true);
                System.out.println("Food placed at: "+row+" "+col);
                placedF++;
            }
        }
    }
    public void displayWorld(Pane root, Runnable onFinished) {
    Cell[][] gridCells = this.gridCells;
    Timeline timeline = new Timeline();
    double delay = 0;
    double delayStep = 8; // ms between each cell

    for (int r = 0; r < gridCells.length; r++) {
        for (int c = 0; c < gridCells[r].length; c++) {

            final int row = r;
            final int col = c;

            KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), e -> {
             Cell cellData = gridCells[row][col];
            Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
            cell.setX(col * CELL_SIZE);
            cell.setY(row * CELL_SIZE);

            if (cellData.isColony()) {
                cell.setFill(Color.web("#000022")); // dark colony
            } else if (cellData.hasFood()) {
                cell.setFill(Color.web("#51827D")); // muted cold green
            } else if (cellData.isObstacle()) {
                cell.setFill(Color.web("#A9BCD0")); // soft stone
            } else {
                cell.setFill(Color.web("#B49A67")); // warm empty ground
            }

                root.getChildren().add(cell);
            });

            timeline.getKeyFrames().add(keyFrame);
            delay += delayStep;
        }
    }
 
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(delay), e -> {
                if (onFinished != null) onFinished.run();
            })
        );

        timeline.play();
    }
}