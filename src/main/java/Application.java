import controller.AbstractController;
import controller.ControllerBombermanGame;

public class Application {
    public static void main(String[] args) {
        AbstractController controller = new ControllerBombermanGame("niveau3");
    }
}
