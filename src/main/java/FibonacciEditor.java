import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Fibonacci extends Application {
    private static final String FILE_NAME = "fibonacci.txt";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        Label number = new Label("Введите число:");
        TextField inputNumber = new TextField();
        inputNumber.setMaxWidth(200);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setMinHeight(20);

        Text progressText = new Text();
        progressText.setTranslateX(40);

        // Окно для вывода ошибок
        Label exceptionText = new Label();

        VBox exceptionWindow = new VBox();
        exceptionWindow.setAlignment(Pos.CENTER);
        exceptionWindow.getChildren().add(exceptionText);

        Stage exceptionStage = new Stage();
        exceptionStage.setTitle("Error");
        exceptionStage.setScene(new Scene(exceptionWindow, 200, 100));

        // Подсчет последовательности
        Button fib = new Button("Fibonacci");
        final String[] resultFib = new String[1];
        final Thread[] count = new Thread[1];
        fib.setOnAction(event -> {
            root.getChildren().removeAll(progressBar, progressText);
            progressBar.setProgress(0);
            progressText.setText(" ");
            try {
                BigInteger num = new BigInteger(inputNumber.getText());
                root.add(progressBar, 0, 5);
                root.add(progressText, 0, 5);
                count[0] = new Thread(() -> resultFib[0] = fibonacciBigNumbers(num, progressBar, progressText));
                count[0].start();
            } catch (NumberFormatException e){
                exceptionText.setText("Неверный формат числа!");
                exceptionStage.show();
            }
        });
        fib.setMinWidth(100);

        Label path = new Label("Введите путь:");
        TextField inputPath = new TextField("E:\\GoJava\\module-8-1\\");
        inputPath.setMaxWidth(200);

        // Сохраняем в файл
        Button saveFib = new Button("Save Fibonacci");
        saveFib.setOnAction(event -> {
            new Thread(() -> {
                try {
                    File file = new File(inputPath.getText() + FILE_NAME);
                    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                    fileWriter.write(resultFib[0]);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    exceptionText.setText("Ошибка записи в файл!");
                    exceptionStage.show();
                }
            }).start();
        });
        saveFib.setMinWidth(100);

        // Прерывание работы потока
        Button cancel = new Button("Cancel count");
        cancel.setOnAction(event -> {
            if (count[0].isAlive()){
                count[0].interrupt();
                exceptionText.setText("Поиск Х остановлен!");
                exceptionStage.show();
            }
        });
        cancel.setMinWidth(100);

        root.add(number, 0, 0);
        root.add(inputNumber, 0, 1);
        root.add(fib, 1, 1);
        root.add(path, 0, 2);
        root.add(inputPath, 0, 3);
        root.add(saveFib, 1, 3);
        root.add(cancel, 0, 4);
        root.setPadding(new Insets(10));
        root.setVgap(10);
        root.setHgap(10);

        primaryStage.setTitle("Fibonacci Editor");
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.show();
    }

    // Метод подсчета последовательности фибоначчи
    private String fibonacciBigNumbers(BigInteger n, ProgressBar progressBar, Text progressText){
        if (n.compareTo(BigInteger.ONE) == 0) {
            return "1";
        }else if ((n.compareTo(BigInteger.valueOf(2)) == 0)) {
            return "1" + " " + "1";
        }else {
            BigInteger a = BigInteger.valueOf(1);
            BigInteger b = BigInteger.valueOf(1);
            String stringFib = a + " " + b;
            for (BigInteger i =  BigInteger.valueOf(2); i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()){ // проверка текущего потока выпонения на Interrupted
                    break;
                }
                BigDecimal percent = new BigDecimal(i).divide(new BigDecimal(n), 2, RoundingMode.HALF_EVEN);
                BigInteger fib = a.add(b);
                stringFib = stringFib.concat(" " + fib);
                a = b;
                b = fib;
                Platform.runLater(() -> {
                    progressBar.setProgress(percent.doubleValue());
                    progressText.setText(Integer.toString((int) (percent.doubleValue() * 100)) + "%");
                });
            }
            return stringFib;
        }
    }
}
