public class Cell {
    private boolean hasFood;
    private boolean isColony;
    private boolean isObstacle;

    public Cell(boolean food, boolean colony, boolean obstacle){
        hasFood = food;
        isColony = colony;
        isObstacle = obstacle;
    }

    public boolean hasFood() {
        return hasFood;
    }

    public void setFood(boolean food) {
        this.hasFood = food;
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
