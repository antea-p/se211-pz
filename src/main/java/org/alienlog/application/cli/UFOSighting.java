package org.alienlog.application.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;
import picocli.CommandLine.ParameterException;

import picocli.CommandLine.Model.CommandSpec;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Command(name = "AlienLog", mixinStandardHelpOptions = true, version = "AlienLog 1.0",
        description = "Allows submission of UFO sighting data.")
public class UFOSighting {
    @Spec CommandSpec spec;

    @Parameters(index = "0", description = "Latitude, in range [-90.0, 90.0]")
    private double latitude;
    @Parameters(index = "1", description = "Longitude, in range [-180.0, 180.0]")
    private double longitude;
    // Instant omogućuje bilježenje egzaktnog trenutka, u UTC-u. https://i.stack.imgur.com/QPhGW.png
    @Parameters(index = "2", description = "Date, in format 1970-01-01T00:00:00Z.")
    private Instant createdAt;
    @Parameters(index = "3", description = "UFO Sighting type. Valid: saucer, abduction, cylindrical_object, close_encounter, unusual_lights",
            converter = CaseInsensitiveUFOSightingTypeConverter.class)
    private UFOSightingType type;

    public UFOSighting() {
    }

    public UFOSighting(double latitude, double longitude, Instant createdAt, UFOSightingType type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public UFOSighting setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public UFOSighting setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UFOSighting setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UFOSightingType getType() {
        return type;
    }

    public UFOSighting setType(UFOSightingType type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UFOSighting that = (UFOSighting) o;
        return Double.compare(that.latitude, latitude) == 0
                && Double.compare(that.longitude, longitude) == 0
                && Objects.equals(createdAt, that.createdAt)
                && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, createdAt, type);
    }

    @Override
    public String toString() {
        return "UFOSighting{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdAt=" + createdAt +
                ", type=" + type +
                '}';
    }

    public void validateDate() {
        try {
            // Assuming createdAt is already set by Picocli
            // This block is just to trigger and catch a DateTimeParseException
            Instant.parse(createdAt.toString());
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid date format: ");
        }
    }

    public void validateLatitude() {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new ParameterException(spec.commandLine(),
                    "Invalid latitude: " + latitude + ". Latitude must be in range [-90.0, 90.0]");
        }
    }

    public void validateLongitude() {
        if (longitude < -180.0 || longitude > 180.0) {
            throw new ParameterException(spec.commandLine(),
                    "Invalid longitude: " + longitude + ". Longitude must be in range [-180.0, 180.0]");
        }
    }

}
