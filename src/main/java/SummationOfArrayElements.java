import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SummationOfArrayElements {
    private ArrayList<Integer> array;
    private int arraySize;
    private int countThreads;

    public SummationOfArrayElements(int arraySize, int countThreads) {
        this.array = new ArrayList<>();
        this.arraySize = arraySize;
        this.countThreads = countThreads;
    }

    // Заполняем массив от 1 до size
    public void arrayFill(){
        for (int i = 0; i < arraySize; i++){
            array.add(i + 1);
        }
    }

    // Подсчет элемента по формуле
    private double count(int x){
        return Math.sin(x) + Math.cos(x);
    }

    // Суммируем элементы в указанном промежутке массива
    public double sumElements(int first, int last){
        double sum = 0;
        for (int i = first; i < last; i++) {
            sum += count(array.get(i));
        }
        return sum;
    }

    // Метод возвращает массив тасков, которые суммируют элементы в указанном промежутке.
    // Количество промежутков равно количеству потоков.
    public ArrayList<FutureTask<Double>> getIntervals() {
        if (countThreads > array.size()){
            System.out.println("Количество потоков больше размера массива!");
            return null;
        }
        int interval = array.size() / countThreads;
        ArrayList<FutureTask<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < array.size();) {
            int finalI = i;
            if (i == ((countThreads * interval) - interval)){
                tasks.add(new FutureTask<>(() -> sumElements(finalI, array.size())));
                break;
            }
            tasks.add(new FutureTask<>(() -> sumElements(finalI, finalI + interval)));
            i += interval;
        }
        return tasks;
    }

    // Запуск подсчета
    public void startCount(ArrayList<FutureTask<Double>> tasks) throws ExecutionException, InterruptedException {
        for (FutureTask<Double> task : tasks) {
            new Thread(task).start();
        }
        getSum(tasks);
    }

    // Повторение подсчетов
    public void startExecutor(int n, ArrayList<FutureTask<Double>> tasks){
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < n; i++){
            pool.submit(() -> {
                try {
                    startCount(tasks);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }

    // Метод извлекает суммы из тасков и выводит общую сумму в консоль
    public void getSum(ArrayList<FutureTask<Double>> tasks) throws ExecutionException, InterruptedException {
        double sum = 0;
        for (int i = 0; i < tasks.size(); i++){
            while (!tasks.get(i).isDone()){
                System.out.println("Ждем, пока FutureTask " + i + " не закончит свое выполнение");
            }
            sum += tasks.get(i).get();
        }
        System.out.println("Сумма элементов: " + sum);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите количество элементов массива: ");
        int size = in.nextInt();
        System.out.print("Введите количество выполняемых потоков: ");
        int countThreads = in.nextInt();
        System.out.print("Введите количество подсчетов: ");
        int n = in.nextInt();
        in.close();

        SummationOfArrayElements summation = new SummationOfArrayElements(size, countThreads);
        summation.arrayFill();

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            long time = System.currentTimeMillis();
            try {
                summation.startCount(summation.getIntervals());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Executor отработал за " + (System.currentTimeMillis() - time) + " миллисекунд");
        });

        pool.submit(() -> {
            long time = System.currentTimeMillis();
            try {
                summation.startExecutor(n, summation.getIntervals());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Executor отработал за " + (System.currentTimeMillis() - time) + " миллисекунд");
        });
        pool.shutdown();
    }
}
