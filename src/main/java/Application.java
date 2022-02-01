import controller.AbstractController;
import controller.ControllerBombermanGame;

import java.net.URISyntaxException;
import java.net.URL;

public class Application {
    public static void main(String[] args) throws URISyntaxException {
        URL url = Application.class.getResource("layouts/niveau3.lay");
        assert url != null;

        AbstractController controller = new ControllerBombermanGame(url.toURI().getPath());
    }
}
