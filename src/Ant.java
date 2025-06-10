import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Ant{
    private int posX, posY;
    private Image ant;
    ImageView antView;
    public Ant(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        init();
    }
    private void init(){
        ant = new Image("file:img/ant.png");
    }
    public void displayAnt(Pane root){
        antView = new ImageView(ant);

        antView.setFitWidth(Matrix.CELL_SIZE);
        antView.setPreserveRatio(true);

        antView.setLayoutX(posX * Matrix.CELL_SIZE);
        antView.setLayoutY(posY * Matrix.CELL_SIZE);
        root.getChildren().add(antView);
    }
    public void setPosition(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    public void moveTo(int newX, int newY, Duration duration) {
        double deltaX = (newX - posX) * Matrix.CELL_SIZE;
        double deltaY = (newY - posY) * Matrix.CELL_SIZE;

        TranslateTransition tt = new TranslateTransition(duration, antView);
        tt.setByX(deltaX);
        tt.setByY(deltaY);
        tt.setOnFinished(e -> {
            antView.setLayoutX(antView.getLayoutX() + deltaX);
            antView.setLayoutY(antView.getLayoutY() + deltaY);
            antView.setTranslateX(0);
            antView.setTranslateY(0);
        });
        tt.play();

        this.posX = newX;
        this.posY = newY;
    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
}