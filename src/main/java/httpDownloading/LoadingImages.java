package httpDownloading;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import httpDownloading.random.RandomGenerator;
import java.net.URL;
import javafx.scene.control.Button;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadingImages extends Application{
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

        VBox menu = new VBox();
        menu.setPadding(new Insets(10));
        menu.setStyle("-fx-background-color: #27333F;");
        menu.setMinWidth(150);
        menu.setSpacing(20);

        // Создаем матрицу из ImageView компонентов и помещаем в GridPane
        ImageView[][] imageViews = new ImageView[N][M];
        GridPane gridPane = new GridPane();
        for (int i = 0; i < N; i++){
            for (int j = 0; j < M; j++){
                ImageView imageView = new ImageView();
                imageView.setFitWidth((WIDTH - MENU_WIDTH) / M);
                imageView.setFitHeight(HEIGHT / M);
                imageViews[i][j] = imageView;
                gridPane.add(imageViews[i][j], j, i);
            }
        }

        // При каждом обновлении, компонентам матрицы назначаются новые картинки
        Button refresh = new Button("Обновить картинки");
        refresh.setOnAction(event -> {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            for (int i = 0; i < N; i++){
                for (int j = 0; j < M; j++){
                    int finalI = i, finalJ = j;
                    pool.submit(() -> loadImage(imageViews[finalI][finalJ]));
                }
            }
            pool.shutdown();
        });

        menu.getChildren().add(refresh);
        root.getChildren().addAll(menu, gridPane);
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.show();
    }

    // Загрузка и назначение картинки елементу матрицы
    private void loadImage(ImageView imageView){
        try {
            URL url = new URL(RandomGenerator.randomURL(LoadingURLs.load(URL_ADDRESSES)));
            Image image = new Image(url.openStream());
            Platform.runLater(() -> imageView.setImage(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
