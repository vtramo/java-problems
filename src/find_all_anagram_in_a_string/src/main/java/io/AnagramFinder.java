package find_all_anagram_in_a_string.src.main.java.io;

import find_all_anagram_in_a_string.src.main.java.io.forkjoin.AnagramFinderForkJoinRecursiveTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class AnagramFinder {
  public enum Version {
    COMPLETABLE_FUTURE(ForkJoinPool.commonPool()),
    VIRTUAL_THREAD(Executors.newVirtualThreadPerTaskExecutor()),
    SEQUENTIAL(null),
    FORK_JOIN(null);

    private final ExecutorService executor;
    Version(final ExecutorService executor) {
      this.executor = executor;
    }
  }

  private final static int START_INDEX_NOT_FOUND = -1;

  private String s, p;

  private final List<Integer> startIndexes = new CopyOnWriteArrayList<>() {
    @Override
    public boolean add(Integer value) {
      if (value == START_INDEX_NOT_FOUND) return false;
      super.add(value);
      return true;
    }
  };

  private void reset(final String s, final String p) {
    this.s = s;
    this.p = p;
    startIndexes.clear();
  }

  public List<Integer> findAnagrams(final String s, final String p, Version version) {
    reset(s, p);
    return switch (version) {
      case COMPLETABLE_FUTURE -> findAnagramsCompletableFutureVersion();
      case VIRTUAL_THREAD -> findAnagramsVirtualThreadsVersion();
      case SEQUENTIAL -> findAnagramsSequentialVersion();
      case FORK_JOIN -> ForkJoinPool.commonPool()
        .invoke(new AnagramFinderForkJoinRecursiveTask(s, p, 0, s.length()))
        .ans();
    };
  }

  public List<Integer> findAnagramsSequentialVersion() {
    if (s.length() < p.length()) return new ArrayList<Integer>();
    char[] sCharArray = s.toCharArray();
    char[] pCharArray = p.toCharArray();
    List<Integer> ans = new ArrayList<Integer>();
    int n = sCharArray.length;
    int m = pCharArray.length;
    int[] pArray = new int[26];
    for (char c : pCharArray) pArray[c - 'a']++;
    int[] curr = new int[26];
    for (int i = 0; i < m - 1; i++) curr[sCharArray[i] - 'a']++;
    for (int i = m - 1, j = 0; i < n; i++, j++) {
      curr[sCharArray[i] - 'a']++;
      boolean canbe = true;
      for (int k = 0; k < 26; k++) {
        if (curr[k] != pArray[k]) {
          canbe = false;
          break;
        }
      }
      if (canbe) ans.add(j);
      curr[sCharArray[j] - 'a']--;
    }
    return ans;
  }

  private List<Integer> findAnagramsCompletableFutureVersion() {
    final CompletableFuture[] futures = IntStream.range(0, s.length())
      .mapToObj(i -> CompletableFuture.supplyAsync(
          () -> findStartingIndexAnagramFrom(i),
          Version.COMPLETABLE_FUTURE.executor
        )
      ).map(future -> future.thenAccept(startIndexes::add))
      .toArray(CompletableFuture[]::new);
    CompletableFuture.allOf(futures).join();
    return startIndexes;
  }

  private List<Integer> findAnagramsVirtualThreadsVersion() {
    List<Future<Integer>> futures = IntStream.range(0, s.length())
      .mapToObj(i -> Version.VIRTUAL_THREAD.executor.submit(
          () -> findStartingIndexAnagramFrom(i)
        )
      ).toList();

    futures.stream()
      .map(future -> {
          try {
            return future.get();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
          }
        }
      ).forEach(startIndexes::add);
    return startIndexes;
  }

  private int findStartingIndexAnagramFrom(final int startIndex) {
    final int pCode = p.chars().reduce(0, Integer::sum);
    final var pSorted = sortString(p);
    final int sLength = s.length();
    int sum = 0;
    for (int i = startIndex; i < sLength; i++) {
      sum += s.charAt(i);
      if (sum == pCode) {
        final var substring = sortString(s.substring(startIndex, i+1));
        if (Objects.equals(pSorted, substring)) return startIndex;
      }
    }
    return START_INDEX_NOT_FOUND;
  }

  private String sortString(final String s) {
    return IntStream.range(0, s.length())
      .mapToObj(s::charAt)
      .sorted()
      .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
      .toString();
  }
}