package jks.lototronback.persistence.user;

import jks.lototronback.controller.login.dto.LoginResponse;
import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.status.Status;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, imports = {Status.class})
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role.name", target = "roleName")
    LoginResponse toLoginResponse(User user);


    @Mapping(source = "userName", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(expression = "java(Status.ACTIVE.getCode())", target = "status")
    User toUser(NewUser newUser);
}