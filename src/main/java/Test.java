import controller.AbstractController;
import controller.ControllerBombermanGame;
import controller.ControllerSimpleGame;

public class Test {
    public static void main(String[] args) {
        AbstractController controller = new ControllerBombermanGame("niveau2");
    }
}
