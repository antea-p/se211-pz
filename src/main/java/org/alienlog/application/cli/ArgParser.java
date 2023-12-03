package org.alienlog.application.cli;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class ArgParser {

    public UFOSighting parse(String[] args) throws IllegalArgumentException {
        if (args.length != 4) {
            System.out.println("Usage: java -jar AlienLogCLI.jar <latitude> <longitude> <date> <sighting_type>");
            throw new IllegalArgumentException("Invalid number of arguments");
        }

        double latitude;
        double longitude;
        Instant date;
        UFOSightingType type;

        try {
            latitude = Double.parseDouble(args[0]);
            longitude = Double.parseDouble(args[1]);

            if (latitude < -90.0 || latitude > 90.0) {
                throw new IllegalArgumentException("Invalid latitude: Latitude should be between -90.0 and 90.0");
            }
            if (longitude < -180.0 || longitude > 180.0) {
                throw new IllegalArgumentException("Invalid longitude: Longitude should be between -180.0 and 180.0");
            }

            String timePattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:(60|61)\\.\\d{2}Z$";
            if (args[2].contains("T24:") || args[2].matches(timePattern)) {
                throw new IllegalArgumentException("Invalid time format: " + args[2]);
            }

            date = Instant.parse(args[2]);

            if (date.atZone(ZoneOffset.UTC).getHour() >= 24 ||
                    date.atZone(ZoneOffset.UTC).getMinute() >= 60 ||
                    date.atZone(ZoneOffset.UTC).getSecond() >= 60) {
                throw new IllegalArgumentException("Invalid time format: " + args[2]);
            }
            type = UFOSightingType.valueOf(args[3].toUpperCase());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinate format: ", e);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: ", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sighting type: ", e);
        }

        return new UFOSighting(latitude, longitude, date, type);
    }

}
