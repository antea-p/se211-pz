package org.alienlog.application.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ArgParserTest {

    ArgParser argParser;

    @BeforeEach
    void setUp() {
        argParser = new ArgParser();
    }

    @Test
    void testCase1() {
        String[] args = {"-91", "181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase2() {
        String[] args = {"91", "-181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase3() {
        String[] args = {"-91", "-181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase4() {
        String[] args = {"91", "181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase5() {
        String[] args = {"-91", "0", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase6() {
        String[] args = {"91", "0", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase7() {
        String[] args = {"0", "-181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase8() {
        String[] args = {"0", "181", "2022-01-01T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testCase9() {
        String[] args = {"0", "0", "2022-01-01T00:00:00Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("2022-01-01T00:00:00Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateJan1st1970Midnight() {
        String[] args = {"0", "0", "1970-01-01T00:00:00Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("1970-01-01T00:00:00Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateDec31st1969OneMinuteToMidnight() {
        String[] args = {"0", "0", "1969-12-31T23:59:59Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("1969-12-31T23:59:59Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateJan19th2038Year2038ProblemStart() {
        String[] args = {"0", "0", "2038-01-19T03:14:07Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("2038-01-19T03:14:07Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateJan19th2038SecondBeforeYear2038Problem() {
        String[] args = {"0", "0", "2038-01-19T03:14:06Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("2038-01-19T03:14:06Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateFeb29th2000LeapYear() {
        String[] args = {"0", "0", "2000-02-29T00:00:00Z", "abduction"};
        UFOSighting actual = argParser.parse(args);
        UFOSighting expected = new UFOSighting(0, 0, Instant.parse("2000-02-29T00:00:00Z"), UFOSightingType.ABDUCTION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDateFeb29th2001NonLeapYear() {
        String[] args = {"0", "0", "2001-02-29T00:00:00Z", "abduction"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> argParser.parse(args));
        assertEquals("Invalid date format: ", exception.getMessage());
    }


    @Test
    void testInvalidDay() {
        String[] args = {"0", "0", "2021-02-31T00:00:00Z", "abduction"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> argParser.parse(args));
        assertEquals("Invalid date format: ", exception.getMessage());
    }

    @Test
    void testInvalidMonth() {
        String[] args = {"0", "0", "2021-13-15T00:00:00Z", "abduction"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> argParser.parse(args));
        assertEquals("Invalid date format: ", exception.getMessage());
    }

    @Test
    void testInvalidHour() {
        String[] args = {"0", "0", "2022-09-15T25:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testInvalidMinute() {
        String[] args = {"0", "0", "2022-09-15T23:60:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testInvalidSecond() {
        String[] args = {"0", "0", "2022-09-15T23:59:61Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testValidSightingType() {
        String[] args = {"0", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertNotNull(argParser.parse(args));
    }

    @Test
    void testInvalidSightingType() {
        String[] args = {"0", "0", "2022-09-15T00:00:00Z", "unknown_type"};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> argParser.parse(args));
        assertEquals("Invalid sighting type: ", exception.getMessage());
    }

    @Test
    void testLatitudeJustAboveLimit() {
        String[] args = {"90.000001", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testLatitudeJustBelowLimit() {
        String[] args = {"-90.000001", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testVeryHighLatitude() {
        String[] args = {"99999999", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testVeryLowLatitude() {
        String[] args = {"-99999999", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testBadLatitudeType() {
        String[] args = {"NotADouble", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testLongitudeJustAboveLimit() {
        String[] args = {"0", "180.000001", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testLongitudeJustBelowLimit() {
        String[] args = {"0", "-180.000001", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testVeryHighLongitude() {
        String[] args = {"0", "99999999", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testVeryLowLongitude() {
        String[] args = {"0", "-99999999", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testBadLongitudeType() {
        String[] args = {"0", "NotADouble", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testWrongDateFormat() {
        String[] args = {"0", "0", "2022-09-15", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testDateEmpty() {
        // Date is empty
        String[] args = {"0", "0", "", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testLatitudeEmpty() {
        String[] args = {"", "0", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testLongitudeEmpty() {
        String[] args = {"0", "", "2022-09-15T00:00:00Z", "abduction"};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testSightingTypeEmpty() {
        String[] args = {"0", "0", "2022-09-15T00:00:00Z", ""};
        assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
    }

    @Test
    void testNoInputData() {
        String[] args = {};
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> argParser.parse(args));
        assertEquals("Invalid number of arguments", exception.getMessage());
    }
}