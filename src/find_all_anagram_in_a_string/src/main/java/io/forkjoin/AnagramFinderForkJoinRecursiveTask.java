package find_all_anagram_in_a_string.src.main.java.io.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

public class AnagramFinderForkJoinRecursiveTask extends RecursiveTask<Result> {

  private final String s, p;
  private final int start, end;

  public AnagramFinderForkJoinRecursiveTask(String s, String p, int start, int end) {
    this.s = s;
    this.p = p;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Result compute() {
    final int length = end - start;
    if (length <= p.length()) {
      return computeSequentially(s, p, start, end);
    }
    final var leftTask = new AnagramFinderForkJoinRecursiveTask(s, p, start, start + length / 2);
    leftTask.fork();
    final var rightTask = new AnagramFinderForkJoinRecursiveTask(s, p, start + length / 2, end);
    final Result rightResult = rightTask.compute();
    final Result leftResult = leftTask.join();
    return combine(leftResult, rightResult);
  }

  private Result combine(Result leftResult, Result rightResult) {
    final int middleStart = leftResult.end() - p.length() + 1;
    final int middleEnd = rightResult.start() + p.length() - 1;
    final var middleResult = computeSequentially(s, p, middleStart, middleEnd);
    return new Result(
      Stream.concat(
        Stream.concat(leftResult.ans().stream(), middleResult.ans().stream()),
        rightResult.ans().stream())
      .toList(),
      leftResult.start(),
      rightResult.end()
    );
  }

  private static Result computeSequentially(String s, String p, int start, int end) {
    if (s.length() < p.length())
      return new Result(new ArrayList<>(), start, end);
    s = s.substring(start, end);
    char[] sCharArray = s.toCharArray();
    char[] pCharArray = p.toCharArray();
    List<Integer> ans = new ArrayList<>();
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
      if (canbe) ans.add(start + j);
      curr[sCharArray[j] - 'a']--;
    }
    return new Result(ans, start, end);
  }
}