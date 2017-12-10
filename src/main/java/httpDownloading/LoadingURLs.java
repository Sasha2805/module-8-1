package httpDownloading;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LoadImageURL {
    public static Set<String> load(String path) throws FileNotFoundException {
        String json = new Scanner(new File(path)).useDelimiter("\\Z").next();
        Set<String> statuses = new HashSet<>();
        statuses.addAll(JSON.parseArray(json, String.class));
        return statuses;
    }
}
