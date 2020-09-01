import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamAPI {
    public static void main(String[] args) throws IOException {
//        int sum = Files.lines(Paths.get("netty/data.txt"))
//                .flatMap(str -> Arrays.stream(str.split(" +")))
//                .map(Integer::parseInt)
//                .filter(val -> val > 3)
//                .sorted()
//                .distinct()
//                .reduce(0, Integer::sum);
        List<Integer> res = Files.lines(Paths.get("netty/data.txt"))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .filter(val -> val > 3)
                .sorted()
                .distinct()
                .collect(Collectors.toList());

        System.out.println(Files.lines(Paths.get("netty/data.txt"))
                .flatMap(str -> Arrays.stream(str.split(" +")))
                .map(Integer::parseInt)
                .filter(val -> val > 3)
                .map(String::valueOf)
                .sorted()
                .distinct()
                .collect(Collectors.joining(", ")));
    }
}
