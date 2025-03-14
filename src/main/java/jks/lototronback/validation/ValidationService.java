package jks.lototronback.validation;

import jks.lototronback.infrastructure.exception.DataNotFoundException;
//import jks.lototronback.persistence.location.Location;

import java.util.List;

import static jks.lototronback.infrastructure.Error.*;

public class ValidationService {

    public static DataNotFoundException throwPrimaryKeyNotFoundException(String primaryKeyName, Integer value) {
        return new DataNotFoundException(PRIMARY_KEY_NOT_FOUND.getMessage() + primaryKeyName + " = " + value, PRIMARY_KEY_NOT_FOUND.getErrorCode());
    }

    public static DataNotFoundException throwForeignKeyNotFoundException(String fieldName, Integer value) {
        return new DataNotFoundException(FOREIGN_KEY_NOT_FOUND.getMessage() + fieldName + " = " + value, FOREIGN_KEY_NOT_FOUND.getErrorCode());
    }

//    public static void validateAtLeastOneLocationExists(List<Location> locations) {
//        if (locations.isEmpty()) {
//            throw new DataNotFoundException(NO_LOCATION_FOUND.getMessage(), NO_LOCATION_FOUND.getErrorCode());
//        }
//    }

}
