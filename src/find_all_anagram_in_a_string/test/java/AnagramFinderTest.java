package find_all_anagram_in_a_string.test.java;

import find_all_anagram_in_a_string.src.main.java.io.AnagramFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class AnagramFinderTest {

  static AnagramFinder.Version version = AnagramFinder.Version.FORK_JOIN;

  AnagramFinder anagramFinder;

  @BeforeEach
  void createNewAnagramFinder() {
    anagramFinder = new AnagramFinder();
  }

  @Test
  void testA() {
    final String s = "fechbcabacxfabocbaaaf";
    final String p = "abc";

    final var indexes = anagramFinder.findAnagrams(s, p, version);

    assertThat(indexes, containsInAnyOrder(4, 5, 7, 15));
  }
}
