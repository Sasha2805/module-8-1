package FxApps;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ComponentsFx {
    public static Button createButton(String name, int minWidth, Runnable runnable){
        Button button = new Button(name);
        button.setMaxWidth(minWidth);
        button.setOnAction(event -> runnable.run());
        return button;
    }

    public static VBox createVbox(int minWidth){
        VBox vBox = new VBox();
        vBox.setMinWidth(minWidth);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(20);
        vBox.setStyle("-fx-background-color: #27333F;");
        return vBox;
    }

    public static ImageView createImageView(int width, int height){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    public static ImageView[][] createArrayImageViews(int width, int height, int n, int m){
        ImageView[][] imageViews = new ImageView[n][m];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                imageViews[i][j] = ComponentsFx.createImageView(width, height);
            }
        }
        return imageViews;
    }

}
