import java.util.Scanner;
import java.util.concurrent.FutureTask;

public class Calculator {
    private double a;
    private double b;
    private String operation;

    public Calculator(double a, double b, String operation) {
        this.a = a;
        this.b = b;
        this.operation = operation;
    }

    public FutureTask<Object> calculate(){
        return new FutureTask<>(() -> {
            if (operation.equals("+")) return sum(a, b);
            if (operation.equals("-")) return diff(a, b);
            if (operation.equals("*")) return multiply(a, b);
            if (operation.equals("/")) return div(a, b);
            if (operation.equals("%")) return mod(a, b);
            if (operation.equals("==")) return equal(a, b);
            if (operation.equals(">")) return more(a, b);
            if (operation.equals("<")) return less(a, b);
            return null;
        });
    }

    public static double sum(double a, double b){
        return a + b;
    }

    public static double diff(double a, double b){
        return a - b;
    }

    public static double multiply(double a, double b){
        return a * b;
    }

    public static double div(double a, double b){
        return a / b;
    }

    public static double mod(double a, double b){
        return a % b;
    }

    public static boolean equal(double a, double b){
        return a == b;
    }

    public static boolean more(double a, double b){
        return a > b;
    }

    public static boolean less(double a, double b){
        return a < b;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите А: ");
        double a = in.nextInt();
        System.out.print("Введите B: ");
        double b = in.nextInt();
        System.out.println("Выберите математическое действие: ");
        System.out.println("1. + " + "\n" + "2. - " + "\n" + "3. * " + "\n" + "4. / ");
        System.out.println("5. % " + "\n" + "6. == " + "\n" + "7. > " + "\n" + "8. < ");
        String operation = in.next();

        Calculator calculator = new Calculator(a, b, operation);
        FutureTask<Object> task = calculator.calculate();
        new Thread(task).start();
        try {
            System.out.println("Результат: " + task.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
