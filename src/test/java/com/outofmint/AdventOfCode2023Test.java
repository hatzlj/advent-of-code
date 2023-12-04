package com.outofmint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdventOfCode2023Test {

    private static final Logger log = LoggerFactory.getLogger(AdventOfCode2023Test.class);
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static Stream<Arguments> test_day1Part1() {
        return Stream.of(
                Arguments.of("/2023/day1-example1.txt", 142),
                Arguments.of("/2023/day1.txt", 54390)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day1Part1(final String inputSource, final int exCalibrationValueChecksum) {
        Pattern digit = Pattern.compile("\\d");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputSource))))) {
            String line;
            int calibrationValueChecksum = 0;
            while ((line = r.readLine()) != null) {
                final Matcher matcher = digit.matcher(line);
                String first = null;
                String last = null;
                while (matcher.find()) {
                    final String current = matcher.group();
                    if (first == null) {
                        first = current;
                    }
                    last = current;
                }
                if (first != null && last != null) {
                    String calibrationValue = first.concat(last);
                    log.info("line: {}", line);
                    log.info("  first: {}", first);
                    log.info("  last: {}", last);
                    log.info("  calibration value: {}", calibrationValue);
                    calibrationValueChecksum += Integer.parseInt(calibrationValue);
                } else {
                    throw new RuntimeException("line: " + line + "\n\tdid not contain calibration values");
                }
            }
            log.info("Calibration value checksum: {}", calibrationValueChecksum);
            assertEquals(exCalibrationValueChecksum, calibrationValueChecksum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Arguments> test_day1Part2() {
        return Stream.of(
                Arguments.of("/2023/day1-example1.txt", 142),
                Arguments.of("/2023/day1-example2.txt", 443),
                Arguments.of("/2023/day1.txt", 54277)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day1Part2(final String inputResource, final int exCalibrationValueChecksum) {
        Pattern digit = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputResource))))) {
            String line;
            int calibrationValueChecksum = 0;
            int lines = 0;
            while ((line = r.readLine()) != null) {
                final Matcher matcher = digit.matcher(line);
                String first = null;
                int firstStart = 0;
                int firstEnd = 0;
                String last = null;
                int lastStart = 0;
                int lastEnd = 0;
                int matchIndex = 0;
                while (matcher.find(matchIndex)) {
                    final String current = matcher.group();
                    matchIndex = matcher.start() + 1;
                    if (first == null) {
                        first = current;
                        firstStart = matcher.start();
                        firstEnd = matcher.end();
                    }
                    last = current;
                    lastStart = matcher.start();
                    lastEnd = matcher.end();
                }

                if (first != null && last != null) {
                    if (firstEnd >= lastStart) {
                        line = line.substring(0, firstStart)
                                .concat(ANSI_RED)
                                .concat(line.substring(firstStart, lastEnd))
                                .concat(ANSI_RESET)
                                .concat(line.substring(lastEnd));
                    } else {
                        line = line.substring(0, firstStart)
                                .concat(ANSI_RED)
                                .concat(line.substring(firstStart, firstEnd))
                                .concat(ANSI_RESET)
                                .concat(line.substring(firstEnd, lastStart))
                                .concat(ANSI_RED)
                                .concat(line.substring(lastStart, lastEnd))
                                .concat(ANSI_RESET)
                                .concat(line.substring(lastEnd));
                    }
                    log.info("line: {}", line);
                    first = translateLiteralNumber(first);
                    last = translateLiteralNumber(last);
                    String calibrationValue = first.concat(last);
                    log.info("  calibration value: {}", calibrationValue);
                    calibrationValueChecksum += Integer.parseInt(calibrationValue);
                    lines++;
                } else {
                    throw new RuntimeException("line: " + line + "\n\tdid not contain calibration values");
                }
            }
            log.info("Processed {} lines", lines);
            log.info("Calibration value checksum: {}", calibrationValueChecksum);
            assertEquals(exCalibrationValueChecksum, calibrationValueChecksum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String translateLiteralNumber(String number) {
        return switch (number) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> number;
        };
    }

    public static Stream<Arguments> test_day2() {
        return Stream.of(
                Arguments.of("/2023/day2-example.txt", 12, 13, 14, 8, 2286),
                Arguments.of("/2023/day2.txt", 12, 13, 14, 2169, 60948)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day2(String inputResource, int numRed, int numGreen, int numBlue, int exGameIdChecksum, int exPowerOfCubesChecksum) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputResource))))) {
            String line;
            int gameIdChecksum = 0;
            int powerOfCubesChecksum = 0;
            while ((line = r.readLine()) != null) {
                String[] game = line.split(": ");
                int gameId = Integer.parseInt(game[0].replace("Game ", ""));
                String[] sets = game[1].split("; ");
                int maxRed = 0;
                int maxGreen = 0;
                int maxBlue = 0;
                for (String set : sets) {
                    String[] colors = set.split(", ");
                    int red = 0;
                    int green = 0;
                    int blue = 0;
                    for (String color : colors) {
                        String[] colorParts = color.split(" ");
                        switch (colorParts[1]) {
                            case "red" -> red = Integer.parseInt(colorParts[0]);
                            case "green" -> green = Integer.parseInt(colorParts[0]);
                            case "blue" -> blue = Integer.parseInt(colorParts[0]);
                            default -> throw new RuntimeException("unknown color");
                        }
                    }
                    maxRed = Math.max(red, maxRed);
                    maxGreen = Math.max(green, maxGreen);
                    maxBlue = Math.max(blue, maxBlue);
                }
                boolean possible = maxRed <= numRed && maxGreen <= numGreen && maxBlue <= numBlue;
                log.info("{}", line);
                log.info("  max: {} red, {} green, {} blue -> {}", highlightIfGreater(maxRed, numRed), highlightIfGreater(maxGreen, numGreen), highlightIfGreater(maxBlue, numBlue), possible ? "possible" : highlight("impossible"));
                if (possible) {
                    gameIdChecksum += gameId;
                }
                powerOfCubesChecksum += (maxRed * maxGreen * maxBlue);
            }
            assertEquals(exGameIdChecksum, gameIdChecksum);
            assertEquals(exPowerOfCubesChecksum, powerOfCubesChecksum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String highlightIfGreater(int current, int allowedMax) {
        return current > allowedMax ? highlight(Integer.toString(current).concat(" (> ").concat(Integer.toString(allowedMax)).concat(")")) : Integer.toString(current);
    }

    private static String highlight(String text) {
        return ANSI_RED.concat(text).concat(ANSI_RESET);
    }

    public static Stream<Arguments> test_day3() {
        return Stream.of(
                Arguments.of("/2023/day3-example.txt", 4361, 467835),
                Arguments.of("/2023/day3.txt", 520019, 75519888)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day3(String inputResource, int exPartNumbersSum, int exGearRatioSum) {
        Pattern symbol = Pattern.compile("[@#$%&*+=\\-/]");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputResource))))) {
            String previous = null;
            String current = null;
            String next;
            int partNumbersSum = 0;
            int gearRatioSum = 0;
            do {
                next = r.readLine();
                if (current != null) {
                    Matcher symbolMatcher = symbol.matcher(current);
                    List<Integer> prevLineNumbers = List.of();
                    List<Integer> curLineNumbers = List.of();
                    List<Integer> nextLineNumbers = List.of();
                    while (symbolMatcher.find()) {
                        int symbolPos = symbolMatcher.start();
                        if (previous != null) {
                            prevLineNumbers = extractAdjacentPartNumbers(previous, symbolPos);
                            partNumbersSum += prevLineNumbers.stream()
                                    .mapToInt(a -> a)
                                    .sum();
                        }
                        curLineNumbers = extractAdjacentPartNumbers(current, symbolPos);
                        partNumbersSum += curLineNumbers.stream()
                                .mapToInt(a -> a)
                                .sum();
                        if (next != null) {
                            nextLineNumbers = extractAdjacentPartNumbers(next, symbolPos);
                            partNumbersSum += nextLineNumbers.stream()
                                    .mapToInt(a -> a)
                                    .sum();
                        }
                        List<Integer> adjacentPartNumbers = new ArrayList<>();
                        adjacentPartNumbers.addAll(prevLineNumbers);
                        adjacentPartNumbers.addAll(curLineNumbers);
                        adjacentPartNumbers.addAll(nextLineNumbers);
                        if ("*".equals(symbolMatcher.group()) && adjacentPartNumbers.size() == 2) {
                            gearRatioSum += adjacentPartNumbers.get(0) * adjacentPartNumbers.get(1);
                        }
                    }
                }
                previous = current;
                current = next;
            } while (current != null);
            assertEquals(exPartNumbersSum, partNumbersSum);
            assertEquals(exGearRatioSum, gearRatioSum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> extractAdjacentPartNumbers(String line, int symbolPos) {
        Pattern number = Pattern.compile("\\d+");
        List<Integer> adjacentPartNumbers = new ArrayList<>();
        Matcher prevNumberMatcher = number.matcher(line);
        while (prevNumberMatcher.find()) {
            if (symbolPos >= (prevNumberMatcher.start()) - 1 && symbolPos <= (prevNumberMatcher.end())) {
                adjacentPartNumbers.add(Integer.parseInt(prevNumberMatcher.group()));
            }
        }
        return adjacentPartNumbers;
    }

    public static Stream<Arguments> test_day4() {
        return Stream.of(
                Arguments.of("/2023/day4-example.txt", 13, 30),
                Arguments.of("/2023/day4.txt", 22193, 5625994)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test_day4(String inputResource, int exCardPointSum, int exTotalNumScratchcards) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023Test.class.getResourceAsStream(inputResource))))) {
            String line;
            int cardPointSum = 0;
            Map<Integer, Integer> copies = new HashMap<>();
            int currentCard = 0;
            while ((line = r.readLine()) != null) {
                final String[] card = line.split(": ");
                final String[] numbers = card[1].split(" \\| ");
                final Set<String> winningNumbers = Set.of(
                        numbers[0].trim().split(" +(?=\\d)")
                );
                final Set<String> myNumbers = Set.of(
                        numbers[1].trim().split(" +(?=\\d)")
                );
                final Set<String> matching = myNumbers.stream()
                        .filter(winningNumbers::contains)
                        .collect(Collectors.toUnmodifiableSet());

                final long matches = matching.size();
                cardPointSum += ((int) Math.pow(2, (matches - 1)));

                copies.put(currentCard, (copies.getOrDefault(currentCard, 0) + 1));

                String outputLine = card[0]
                        .concat(": ")
                        .concat(winningNumbers.stream()
                                .mapToInt(Integer::parseInt)
                                .sorted()
                                .mapToObj(Integer::toString)
                                .map(s -> String.format("%1$2s", s))
                                .collect(Collectors.joining(" ")))
                        .concat(" | ")
                        .concat(myNumbers.stream()
                                .mapToInt(Integer::parseInt)
                                .sorted()
                                .mapToObj(Integer::toString)
                                .map(s -> String.format("%1$2s", s))
                                .collect(Collectors.joining(" ")))
                        .concat(" => ")
                        .concat(Integer.toString(copies.get(currentCard)))
                        .concat(" copies");
                for (String m : matching) {
                    outputLine = outputLine.replaceAll("(?<=\\s)+" + m + "((?=\\s+)|(?=$))", highlight(m));
                }
                log.info("{}", outputLine);
                log.debug("  {}", line);

                log.debug("  current card total: {}", copies.get(currentCard));

                for (int i = currentCard + 1; i <= currentCard + matches; i++) {
                    copies.put(i, (copies.getOrDefault(i, 0) + copies.get(currentCard)));
                    log.debug("  upcoming card {} total: {}", i, copies.get(i));
                }
                currentCard++;

                log.debug("");
            }
            assertEquals(exCardPointSum, cardPointSum);
            assertEquals(exTotalNumScratchcards, copies.values().stream().mapToInt(i -> i).sum());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}