import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lambdas {

    public static void main(String[] args) {

        Runnable runnable = () -> {

        };

        Callable<String> callable = () -> {
            return null;
        };

        Consumer<String> consumer = System.out::println; // forEach

        Predicate<String> predicate = sss -> sss.contains("OK"); // filter, noneMatch, anyMatch, allMatch

        Supplier<String> supplier = () -> "123"; // reduce

        Supplier<HashMap<String, Integer>> mapSupplier = HashMap::new; // reduce

        Function<String, Integer> stringIntegerFunction = param -> param.length(); // map
    }
}
