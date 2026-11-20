import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FunctionalTasks {

    // Завдання 1: елементи під непарними індексами (1, 3, 5...) у форматі "1. Ivan, 3. Peter"
    public static String formatOddIndexedNames(List<String> names) {
        return IntStream.range(0, names.size())
                .filter(index -> index % 2 != 0)
                .mapToObj(index -> index + ". " + names.get(index))
                .collect(Collectors.joining(", "));
    }

    // Завдання 2: верхній регістр і сортування за спаданням (від Z до A)
    public static List<String> upperCaseSortedDesc(List<String> items) {
        return items.stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // Завдання 3: дістаємо всі числа з рядків масиву, сортуємо та об'єднуємо через ", "
    public static String extractAndSortNumbers(String[] array) {
        return Stream.of(array)
                .flatMap(s -> Stream.of(s.split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    // Завдання 4: лінійний конгруентний генератор через Stream.iterate
    // Формула: x_{n+1} = (a * x_n + c) mod m
    public static Stream<Long> lcgStream(long seed, long a, long c, long m) {
        BigInteger bigA = BigInteger.valueOf(a);
        BigInteger bigC = BigInteger.valueOf(c);
        BigInteger bigM = BigInteger.valueOf(m);

        return Stream.iterate(seed, x ->
                BigInteger.valueOf(x)
                        .multiply(bigA)
                        .add(bigC)
                        .mod(bigM)
                        .longValue()
        );
    }

    // Завдання 5: чергування елементів двох стрімів до завершення коротшого
    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
    Iterator<T> it1 = first.iterator();
    Iterator<T> it2 = second.iterator();

    Iterator<T> zippedIterator = new Iterator<>() {
        private boolean takeFromFirst = true;

        @Override
        public boolean hasNext() {
            return takeFromFirst ? it1.hasNext() : it2.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            if (takeFromFirst) {
                takeFromFirst = false;
                return it1.next();
            } else {
                takeFromFirst = true;
                return it2.next();
            }
        }
    };

    Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(
            zippedIterator,
            Spliterator.ORDERED | Spliterator.NONNULL
    );

    return StreamSupport.stream(spliterator, false);
}

    public static void main(String[] args) {
        // Тест 1
        List<String> names = List.of("Ivan", "Peter", "Anna", "John", "Oleh");
        System.out.println("Завдання 1: " + formatOddIndexedNames(names));

        // Тест 2
        System.out.println("Завдання 2: " + upperCaseSortedDesc(names));

        // Тест 3
        String[] rawNumbers = {"1, 2, 0", "4, 5"};
        System.out.println("Завдання 3: " + extractAndSortNumbers(rawNumbers));

        // Тест 4
        long a = 25214903917L;
        long c = 11L;
        long m = 1L << 48; // 2^48
        List<Long> randomTen = lcgStream(1L, a, c, m)
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("Завдання 4: " + randomTen);

        // Тест 5
        Stream<String> s1 = Stream.of("A", "B", "C", "D");
        Stream<String> s2 = Stream.of("1", "2", "3");
        List<String> zipped = zip(s1, s2).collect(Collectors.toList());
        System.out.println("Завдання 5: " + zipped);
    }
}
