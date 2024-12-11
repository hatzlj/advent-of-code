package com.outofmint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventOfCode2024Test {

    private static final Logger log = LoggerFactory.getLogger(AdventOfCode2024Test.class);
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";


    @ParameterizedTest
    @MethodSource
    public void test_day1Part1(final String inputSource, final int exAbsoluteDistance) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            List<Integer> list1 = new LinkedList<>();
            List<Integer> list2 = new LinkedList<>();
            while ((line = r.readLine()) != null) {
                Iterator<Integer> parts = Arrays.stream(line.split(" {3}")).map(Integer::parseInt).iterator();
                list1.add(parts.next());
                list2.add(parts.next());
            }
            List<Integer> sorted1 = list1.stream().sorted().toList();
            List<Integer> sorted2 = list2.stream().sorted().toList();
            int absoluteDistance = 0;
            for (int i = 0; i < sorted1.size(); i++) {
                absoluteDistance += Math.abs(sorted1.get(i) - sorted2.get(i));
            }
            assertEquals(exAbsoluteDistance, exAbsoluteDistance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day1Part1() {
        return Stream.of(
                Arguments.of("/2024/day1-example.txt", 11),
                Arguments.of("/2024/day1-input.txt", 3569916)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day1Part2(final String inputSource, final int exSimilarityScore) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            List<Integer> numbers = new LinkedList<>();
            Map<Integer, Integer> frequency = new HashMap<>();
            while ((line = r.readLine()) != null) {
                Iterator<Integer> parts = Arrays.stream(line.split(" {3}")).map(Integer::parseInt).iterator();
                numbers.add(parts.next());
                frequency.compute(parts.next(), (k, v) -> v == null ? 1 : ++v);
            }
            int similarityScore = numbers.stream()
                    .mapToInt(n -> n * frequency.getOrDefault(n, 0))
                    .sum();
            assertEquals(exSimilarityScore, similarityScore);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day1Part2() {
        return Stream.of(
                Arguments.of("/2024/day1-example.txt", 31),
                Arguments.of("/2024/day1-input.txt", 26407426)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day2Part1(final String inputSource, final int exValidReports) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            int validReports = 0;
            while ((line = r.readLine()) != null) {
                Integer[] report = Arrays.stream(line.split(" +")).map(Integer::parseInt).toArray(Integer[]::new);
                boolean valid = true;
                if (report.length > 1) {
                    boolean increasing = report[0] < report[1];
                    for (int i = 1; i < report.length; i++) {
                        if (increasing && report[i - 1] > report[i]) {
                            valid = false;
                            break;
                        } else if (!increasing && report[i - 1] < report[i]) {
                            valid = false;
                            break;
                        } else if (Math.abs(report[i - 1] - report[i]) < 1 || Math.abs(report[i - 1] - report[i]) > 3) {
                            valid = false;
                            break;
                        }
                    }
                }
                validReports += valid ? 1 : 0;
            }
            assertEquals(exValidReports, validReports);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day2Part1() {
        return Stream.of(
                Arguments.of("/2024/day2-example.txt", 2),
                Arguments.of("/2024/day2-input.txt", 230)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day2Part2(final String inputSource, final int exValidReports) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            int validReports = 0;
            while ((line = r.readLine()) != null) {
                Integer[] report = Arrays.stream(line.split(" +")).map(Integer::parseInt).toArray(Integer[]::new);
                boolean valid = checkReport(report);
                if (!valid) {
                    for (int i = 0; i < report.length; i++) {
                        List<Integer> subset = new ArrayList<>(Arrays.asList(Arrays.copyOf(report, report.length)));
                        subset.remove(i);
                        valid = checkReport(subset.toArray(Integer[]::new));
                        if (valid) {
                            break;
                        }
                    }
                }
                validReports += valid ? 1 : 0;
            }
            assertEquals(exValidReports, validReports);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkReport(Integer[] report) {
        boolean valid = true;
        if (report.length > 1) {
            boolean increasing = report[0] < report[1];
            for (int i = 1; i < report.length; i++) {
                if (increasing && report[i - 1] > report[i]) {
                    valid = false;
                    break;
                } else if (!increasing && report[i - 1] < report[i]) {
                    valid = false;
                    break;
                } else if (Math.abs(report[i - 1] - report[i]) < 1 || Math.abs(report[i - 1] - report[i]) > 3) {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    public static Stream<Arguments> test_day2Part2() {
        return Stream.of(
                Arguments.of("/2024/day2-example.txt", 4),
                Arguments.of("/2024/day2-input.txt", 301)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day3Part1(final String inputSource, final int exMulSum) {
        final Pattern mulPattern = Pattern.compile("mul\\(\\d+\\,\\d+\\)");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            int acMulSum = 0;
            while ((line = r.readLine()) != null) {
                Matcher mulMatcher = mulPattern.matcher(line);
                while (mulMatcher.find()) {
                    String mulEx = mulMatcher.group()
                            .replaceAll("mul\\(", "")
                            .replaceAll("\\)", "");
                    Integer[] operands = Arrays.stream(mulEx.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
                    acMulSum += operands[0] * operands[1];
                }
            }
            assertEquals(exMulSum, acMulSum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day3Part1() {
        return Stream.of(
                Arguments.of("/2024/day3-example.txt", 161),
                Arguments.of("/2024/day3-input.txt", 175615763)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day3Part2(final String inputSource, final int exMulSum) {
        final Pattern expressionPattern = Pattern.compile("(mul\\(\\d+\\,\\d+\\))|(don't\\(\\))|(do\\(\\))");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            int acMulSum = 0;
            boolean doIt = true;
            while ((line = r.readLine()) != null) {
                Matcher expressionMatcher = expressionPattern.matcher(line);
                while (expressionMatcher.find()) {
                    String expression = expressionMatcher.group();
                    String operation = expression.replaceAll("\\(.*\\)", "");
                    switch (operation.toLowerCase()) {
                        case "mul":
                            if (doIt) {
                                String mulEx = expression
                                        .replaceAll("mul\\(", "")
                                        .replaceAll("\\)", "");
                                Integer[] operands =
                                        Arrays.stream(mulEx.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
                                acMulSum += operands[0] * operands[1];
                            }
                            break;
                        case "do":
                            doIt = true;
                            break;
                        case "don't":
                            doIt = false;
                            break;
                        default:
                            throw new UnsupportedOperationException("Operation not supported: " + operation);
                    }
                }
            }
            assertEquals(exMulSum, acMulSum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day3Part2() {
        return Stream.of(
                Arguments.of("/2024/day3-example2.txt", 48)
                , Arguments.of("/2024/day3-input.txt", 74361272)
        );
    }
}