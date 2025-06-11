import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Matrix{
    private Cell[][] gridCells;
    private Rectangle[][] cellViews;
    int cols, rows;
    
    public static final int CELL_SIZE = 20;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int COLONY_X = 20;
    public static final int COLONY_Y = 15;

    public Matrix(){        
        cols = WIDTH / CELL_SIZE;
        rows = HEIGHT / CELL_SIZE;

        gridCells = new Cell[rows][cols];
        init();
    }
    public boolean isObstacle(int x, int y) {
        if(inBounds(x, y)){
            return gridCells[y][x].isObstacle();
        }else{return false;}
    }
    public boolean isFood(int x, int y) {
        if(inBounds(x, y)){
            return gridCells[y][x].isFood();
        }else{return false;}
    }
    public void setFood(int x, int y, boolean food){
        gridCells[y][x].setFood(food);
        this.updateCell(x, y);
    }
    public boolean isColony(int x, int y) {
        if(inBounds(x, y)){
            return gridCells[y][x].isColony();
        }else{return false;}
    }
    public boolean inBounds(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }
    private boolean freeCell(int x, int y){
        if(!this.isColony(x, y) && !this.isFood(x, y) && !this.isObstacle(x, y)){
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
        gridCells[COLONY_Y][COLONY_X].setColony(true);
        cellViews = new Rectangle[rows][cols];
        System.out.println("Colony placed at: (" + COLONY_Y + ", " + COLONY_Y + ")");
    }
    private void updateCell(int x, int y) {
        Rectangle cell = cellViews[y][x];
        if (cell == null) return;

        if (isFood(x, y)) {
            cell.setFill(Color.web("#51827D"));
        } else if (isObstacle(x, y)) {
            cell.setFill(Color.web("#A9BCD0"));
        } else {
            cell.setFill(Color.web("#B49A67"));
        }
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
        double delayStep = 1.5; // milliseconds per cell

        for (int r = 0; r < gridCells.length; r++) {
            for (int c = 0; c < gridCells[r].length; c++) {

                final int row = r;
                final int col = c;

                KeyFrame keyFrame = new KeyFrame(Duration.millis(delay), e -> {

                    Cell cellData = gridCells[row][col];
                    Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                    cell.setX(col * CELL_SIZE);
                    cell.setY(row * CELL_SIZE);
                    cellViews[row][col] = cell;


                    // Add terrain color and colony image if needed
                    if (cellData.isColony()) {
                        cell.setFill(Color.web("#B49A67"));

                        Image colony = new Image("file:img/colony.png");
                        ImageView colonyView = new ImageView(colony);
                        colonyView.setFitWidth(CELL_SIZE);
                        colonyView.setFitHeight(CELL_SIZE);
                        colonyView.setX(col * CELL_SIZE);
                        colonyView.setY(row * CELL_SIZE);

                        root.getChildren().add(cell);
                        root.getChildren().add(colonyView); // Colony image on top
                    } else {
                        if (cellData.isFood()) {
                            cell.setFill(Color.web("#51827D")); // food
                        } else if (cellData.isObstacle()) {
                            cell.setFill(Color.web("#A9BCD0")); // rock
                        } else {
                            cell.setFill(Color.web("#B49A67")); // ground
                        }

                        root.getChildren().add(cell);
                    }
                });

                timeline.getKeyFrames().add(keyFrame);
                delay += delayStep;
            }
        }

        // Trigger onFinished when grid finishes drawing
        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(delay), e -> {
                if (onFinished != null) onFinished.run();
            })
        );

        timeline.play();
    }
    public List<int[]> findShortestPath(int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.add(new Node(startX, startY, 0, null));

        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}}; // Right, Left, Down, Up

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (visited[current.y][current.x]) continue;
            visited[current.y][current.x] = true;

            if (current.x == goalX && current.y == goalY) {
                List<int[]> path = new ArrayList<>();
                while (current != null) {
                    path.add(0, new int[]{current.x, current.y});
                    current = current.parent;
                }
                return path;
            }

            for (int[] d : directions) {
                int nx = current.x + d[0];
                int ny = current.y + d[1];

                if (inBounds(nx, ny) && !visited[ny][nx] && !isObstacle(nx, ny)) {
                    queue.add(new Node(nx, ny, current.cost + 1, current));
                }
            }
        }

        return null; // No path found
    }
    private class Node implements Comparable<Node> {
        int x, y, cost;
        Node parent;

        public Node(int x, int y, int cost, Node parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
    private class Cell {
        private boolean isFood;
        private boolean isColony;
        private boolean isObstacle;

        public Cell(boolean food, boolean colony, boolean obstacle){
            isFood = food;
            isColony = colony;
            isObstacle = obstacle;
        }

        public boolean isFood() {
            return isFood;
        }

        public void setFood(boolean food) {
            this.isFood = food;
        }

        public boolean isColony() {
            return isColony;
        }

        public void setColony(boolean colony) {
            this.isColony = colony;
        }
        public boolean isObstacle(){
            return isObstacle;
        }
        public void setObstacle(boolean obstacle){
            this.isObstacle = obstacle;
        }
    }
}