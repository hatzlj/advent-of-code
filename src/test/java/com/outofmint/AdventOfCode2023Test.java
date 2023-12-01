package com.outofmint;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
}