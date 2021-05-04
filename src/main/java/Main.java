import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static final Integer MAX = 10;

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    public static final List<Integer> RANDOM_ELEMENTS = getRandomElements();

    public static void main(String[] args) {
        Splay splay = new Splay();

        Long timeBeforeInsertion = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            System.out.println("=================");
            splay.insert(RANDOM_ELEMENTS.get(i));
            System.out.println(RANDOM_ELEMENTS.get(i));
            System.out.println();
            splay.printTree();
            System.out.println("=================");
            System.out.println();
            System.out.println();
            System.out.println();
        }
        Long timeAfterInsertion = System.currentTimeMillis();

        Long elapsedTime = timeAfterInsertion - timeBeforeInsertion;
        System.out.println(MessageFormat.format("{0} sec(s)", elapsedTime));



        System.out.println("=================");
        for (var node : splay) {
            System.out.println(node);
        }
        System.out.println("=================");

//        System.out.println("=================");
//        var iterator = splay.iterator();
//        while (iterator.hasNext()) {
//            Integer node = iterator.next();
//            System.out.println(node);
//        }
//        System.out.println("=================");
    }

    public static List<Integer> getRandomElements() {
        return IntStream.range(0, MAX).mapToObj(i -> RANDOM.nextInt(10000)).collect(Collectors.toList());
    }
}
