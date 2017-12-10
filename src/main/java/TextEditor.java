package FxApps;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Scanner;

public class TextEditor extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final String FILE_PATH = "E:\\GoJava\\document.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        TextField inputPath = new TextField();
        inputPath.setMinWidth(WIDTH / 3);
        inputPath.setMinHeight(20);
        inputPath.setTranslateX(10);
        inputPath.setTranslateY(10);

        TextArea textArea = new TextArea();
        textArea.setMinWidth(WIDTH - 100);
        textArea.setMinHeight(HEIGHT - 200);
        textArea.setTranslateX(10);
        textArea.setTranslateY(inputPath.getTranslateY() + inputPath.getMinHeight() + 10);

        Button loadFile = new Button("Load");
        loadFile.setOnAction(event -> {
                new Thread(() -> {
                    try {
                        URL website = new URL(inputPath.getText());
                        ReadableByteChannel channel = Channels.newChannel(website.openStream());
                        FileOutputStream stream = new FileOutputStream(FILE_PATH);
                        stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                        System.out.println("Загрузка завершилась успешно");
                        String file = new Scanner(new File(FILE_PATH)).useDelimiter("\\Z").next();
                        textArea.setText(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Загрузка завершилась с ошибкой...");
                    }
                }).start();
        });
        loadFile.setTranslateX(inputPath.getTranslateX() + inputPath.getMinWidth() + 10);
        loadFile.setTranslateY(10);
        loadFile.setMaxWidth(100);

        Button save = new Button("Save");
        save.setOnAction(event -> {
            new Thread(() -> {
                try {
                    File file = new File(FILE_PATH);
                    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                    fileWriter.write(textArea.getText());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        save.setTranslateY(inputPath.getMaxHeight() + textArea.getMinHeight() + 50);
        save.setTranslateX(10);
        save.setMaxWidth(100);

        root.getChildren().addAll(inputPath, loadFile, textArea, save);
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.show();
    }
}
