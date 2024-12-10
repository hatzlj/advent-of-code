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
            assertEquals(absoluteDistance, exAbsoluteDistance);
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
            assertEquals(similarityScore, exSimilarityScore);
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
}