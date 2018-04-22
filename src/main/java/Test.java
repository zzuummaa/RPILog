import ru.zuma.utils.JSONConverter;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        Map<Integer, String> map = new HashMap<>();
//        map.put(1, "one");
//        map.put(2, "two");
//
//        String json = JSONConverter.fromLogRecords(map);
//        System.out.println(json);

        String json = "{\"log\":\"Hello rpi log!\"}";
        String log = JSONConverter.jsonToLog(json);
        System.out.println(log);
    }
}
