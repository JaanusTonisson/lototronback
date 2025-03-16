package jks.lototronback.persistence.userimage;

import jks.lototronback.controller.profile.dto.ProfileInfo;
import jks.lototronback.util.BytesConverter;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserImageMapper {

    @Named("toData")
    static byte[] toData(String imageData) {
        return BytesConverter.stringToBytesArray(imageData);
    }
}