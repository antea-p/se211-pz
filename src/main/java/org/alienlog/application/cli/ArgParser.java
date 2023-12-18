package org.alienlog.application.cli;

import picocli.CommandLine;

public class ArgParser {
    public UFOSighting parse(String[] args) throws IllegalArgumentException {
        UFOSighting ufoSighting = new UFOSighting();
        CommandLine cmd = new CommandLine(ufoSighting);
        try {
            cmd.parseArgs(args);
            ufoSighting.validateLatitude();
            ufoSighting.validateLongitude();
            return ufoSighting;

        } catch (CommandLine.ParameterException ex) {
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

