import java.util.ArrayList;
import java.util.concurrent.FutureTask;

public class CountingNumbersInArray {
    private ArrayList<Integer> array;
    int arraySize;
    int countThreads;

    public CountingNumbersInArray(int arraySize, int countThreads) {
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

    // Метод расбивает массив на количество промежутков, равных количеству потоков.
    // Каждый поток будет суммировать элементы в своем промежутке.
    public ArrayList<FutureTask<Double>> distributionOfIntervals() {
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
                System.out.println(finalI+" "+array.size());
                break;
            }
            tasks.add(new FutureTask<>(() -> sumElements(finalI, finalI + interval)));
            System.out.println(finalI+" "+(finalI + interval));
            i += interval;
        }
        return tasks;
    }

    // Суммируем элементы в указанном промежутке массива
    public double sumElements(int first, int last){
        double sum = 0;
        for (int i = first; i < last; i++) {
            sum += count(array.get(i));
        }
        return sum;
    }

    public static void main(String[] args) {
        CountingNumbersInArray counting = new CountingNumbersInArray(8_000_000, 3);
        counting.arrayFill();
        counting.distributionOfIntervals();
    }
}
