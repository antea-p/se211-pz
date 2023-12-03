package org.alienlog.application.cli;

import java.time.Instant;
import java.util.Objects;

public class UFOSighting {

    private double latitude, longitude;
    // Instant omogućuje bilježenje egzaktnog trenutka, u UTC-u. https://i.stack.imgur.com/QPhGW.png
    private Instant createdAt;
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
}
