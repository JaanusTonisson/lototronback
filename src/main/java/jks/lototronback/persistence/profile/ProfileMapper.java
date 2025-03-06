package jks.lototronback.persistence.profile;


import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.persistence.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;



@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {


@Mapping(source = "firstName", target = "firstName")
@Mapping(source = "lastName", target = "lastName")
@Mapping(source = "phoneNumber", target = "phoneNumber")
    Profile toProfile(NewUser newUser);
}
