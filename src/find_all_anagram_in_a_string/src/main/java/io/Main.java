package find_all_anagram_in_a_string.src.main.java.io;


public class Main {

    public static void main(String[] args) {
        final var s = new StringBuilder(Inputs.s);

        final AnagramFinder anagramFinder = new AnagramFinder();

        long t0 = System.currentTimeMillis();
        System.out.println(anagramFinder
          .findAnagrams(
            s.toString(),
            Inputs.p,
            AnagramFinder.Version.COMPLETABLE_FUTURE
          )
        );
        long t1 = System.currentTimeMillis();
        System.out.println("ForkJoinPool: " + (t1 - t0) + "ms");

        t0 = System.currentTimeMillis();
        System.out.println(anagramFinder
          .findAnagrams(
            s.toString(),
            Inputs.p,
            AnagramFinder.Version.VIRTUAL_THREAD
          )
        );
        t1 = System.currentTimeMillis();
        System.out.println("VirtualThreadPerTaskExecutor: " + (t1 - t0) + "ms");

        t0 = System.currentTimeMillis();
        System.out.println(anagramFinder
          .findAnagrams(
            s.toString(),
            Inputs.p,
            AnagramFinder.Version.SEQUENTIAL
          )
        );
        t1 = System.currentTimeMillis();
        System.out.println("Sequential Version: " + (t1 - t0) + "ms");

        t0 = System.currentTimeMillis();
        System.out.println(anagramFinder
          .findAnagrams(
            s.toString(),
            Inputs.p,
            AnagramFinder.Version.FORK_JOIN
          )
        );
        t1 = System.currentTimeMillis();
        System.out.println("ForkJoin Version: " + (t1 - t0) + "ms");
    }
}
