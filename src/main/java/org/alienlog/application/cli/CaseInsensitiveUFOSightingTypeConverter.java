package org.alienlog.application.cli;

import picocli.CommandLine;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.TypeConversionException;

public class CaseInsensitiveUFOSightingTypeConverter implements ITypeConverter<UFOSightingType> {
    @Override
    public UFOSightingType convert(String value) throws Exception {
        for (UFOSightingType type : UFOSightingType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TypeConversionException("Invalid value for UFOSightingType: " + value);
    }
}
