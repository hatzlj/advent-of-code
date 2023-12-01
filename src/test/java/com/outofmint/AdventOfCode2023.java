package com.outofmint;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdventOfCode2023 {

    private static final Logger log = LoggerFactory.getLogger(AdventOfCode2023.class);

    @Test
    public void day1() {
        Pattern digit = Pattern.compile("\\d");
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(AdventOfCode2023.class.getResourceAsStream("/2023/day1.txt"))))) {
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
                if(first !=null && last!=null) {
                    String calibrationValue = first.concat(last);
                    log.info("line: {}", line);
                    log.info("\t first: {}", first);
                    log.info("\t last: {}", last);
                    log.info("\t calibration value: {}", calibrationValue);
                    calibrationValueChecksum += Integer.parseInt(calibrationValue);
                } else {
                    throw new RuntimeException("line: "+ line + "\n\tdid not contain calibration values");
                }
            }
            log.info("Calibration value checksum: {}", calibrationValueChecksum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
