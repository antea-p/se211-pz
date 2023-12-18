package org.alienlog.application.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParameterException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class ArgParser {
    public UFOSighting parse(String[] args) throws IllegalArgumentException {
        UFOSighting ufoSighting = new UFOSighting();
        CommandLine cmd = new CommandLine(ufoSighting);
        try {
            cmd.parseArgs(args);
            ufoSighting.validateDate();
            ufoSighting.validateLatitude();
            ufoSighting.validateLongitude();
            return ufoSighting;

        } catch (CommandLine.ParameterException ex) {
            // Handle other parameter exceptions
            String message = ex.getMessage();
            if (args.length != 4) {
                cmd.usage(System.err);
                throw new IllegalArgumentException("Invalid number of arguments");
            }
            else if (message.contains("Instant")) {
                throw new IllegalArgumentException("Invalid date format: ");
            }
            throw new IllegalArgumentException("Invalid sighting type: ");
        }
    }
}

