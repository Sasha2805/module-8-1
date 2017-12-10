package httpDownloading;

import FxApps.ComponentsFx;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import random.RandomGenerator;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpDownloading extends Application{
    private static final String URL_ADDRESSES = "src/main/java/httpDownloading/filesJSON/pictureAddresses.json";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final int MENU_WIDTH = 150;
    private static final int N = 5;
    private static final int M = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        VBox vBox = ComponentsFx.createVbox(MENU_WIDTH);

        // Создаем матрицу из ImageView компонентов
        ImageView[][] imageViews = ComponentsFx.createArrayImageViews((WIDTH - MENU_WIDTH)/M, HEIGHT/M, N, M);
        GridPane gridPane = new GridPane();
        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                gridPane.add(imageViews[i][j], j, i);
            }
        }

        // При каждом обновлении, компонентам матрицы назначаются новые картинки
        vBox.getChildren().add(ComponentsFx.createButton("Обновить", 100, () -> {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            for (int i = 0; i < N; i++){
                for (int j = 0; j < M; j++){
                    int finalI = i, finalJ = j;
                    pool.submit(() -> loadImage(imageViews[finalI][finalJ]));
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

    // Загрузка и назначение картинки елементу матрицы
    private void loadImage(ImageView imageView){
        try {
            URL url = new URL(RandomGenerator.randomURL(LoadImageURL.load(URL_ADDRESSES)));
            Image image = new Image(url.openStream());
            Platform.runLater(() -> imageView.setImage(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
