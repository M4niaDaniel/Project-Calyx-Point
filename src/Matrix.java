import java.util.Random;

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
    public void generateWorld(String seed){
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
    
        while (placedR <= 0) {
            int index = rand.nextInt(patterns.length);
            int[][] pattern = patterns[index];

            int startRow = rand.nextInt(rows-3);
            int startCol = rand.nextInt(rows-3);

            for(int[] offset : pattern){
                int row = startRow + offset[0];
                int col = startCol + offset[1];
                if(!gridCells[row][col].isColony() && !gridCells[row][col].isObstacle()){
                    gridCells[row][col].setObstacle(true);
                    System.out.println("Rock placed at: "+row+" "+col);
                    placedR++;
                }
            }
        }

        int placedF = 0;
        while (placedF <= 20) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
    
            if (!gridCells[row][col].isColony() && !gridCells[row][col].hasFood() && !gridCells[row][col].isObstacle()) {
                gridCells[row][col].setFood(true);
                System.out.println("Food placed at: "+row+" "+col);
                placedF++;
            }
        }
    }
}