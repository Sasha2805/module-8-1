package FxApps;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpDownloading extends Application{
    private static final int WIDTH = 1000;
    private static final int MENU_WIDTH = 150;
    private static final int HEIGHT = 700;
    private static final int N = 5;
    private static final int M = 5;
    private ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        VBox vBox = ComponentsFx.createVbox(MENU_WIDTH);

        GridPane gridPane = new GridPane();
        ImageView[][] imageViews = ComponentsFx.createArrayImageViews((WIDTH - MENU_WIDTH)/N, HEIGHT/N, N, M);

        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                gridPane.add(imageViews[i][j], j, i);
            }
        }



        vBox.getChildren().add(ComponentsFx.createButton("Обновить", 100, () -> {
            for (int i = 0; i < N; i++){
                for (int j = 0; j < M; j++){
                    int finalI = i;
                    int finalJ = j;
                    pool.submit(() -> {
                        try {
                            URL url = new URL("https://s1.1zoom.ru/big0/106/Clock_Watch_Switzerland_Vacheron_Constantin_Hands_535869_1280x800.jpg");
                            Image image = new Image(url.openStream());
                            Platform.runLater(() -> imageViews[finalI][finalJ].setImage(image));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            pool.shutdown();
        }));

        root.getChildren().addAll(vBox, gridPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();

    }

}
