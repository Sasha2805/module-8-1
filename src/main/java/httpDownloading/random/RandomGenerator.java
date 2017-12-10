package random;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class RandomGenerator {
    private static final Random random = new Random();

    public static int random(int min, int max){
        return (int) (min + Math.random() * (max - min));
    }

    public static String randomURL(Set<String> set){
        int index = RandomGenerator.random(0, set.size());
        Iterator<String> iterator = set.iterator();
        for (int i = 0; i <= index; i++){
            if (iterator.hasNext()){
                String address = iterator.next();
                if (i == index){
                    return address;
                }
            }
        }
        return "";
    }

}
