import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Ant{
    int posX, posY;
    private Image ant;
    public Ant(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        init();
    }
    private void init(){
        ant = new Image("file:img/ant.png");
    }
    public void displayAnt(Pane pane){
        ImageView imageView = new ImageView(ant);
        imageView.setLayoutX(posX * Matrix.CELL_SIZE);
        imageView.setLayoutY(posY * Matrix.CELL_SIZE);
        pane.getChildren().add(imageView);
    }
    public void setPosition(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
}