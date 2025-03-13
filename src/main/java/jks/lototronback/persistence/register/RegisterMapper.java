package jks.lototronback.persistence.register;

import jks.lototronback.controller.register.RegisterInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RegisterMapper {


    @Mapping(source = "lunchEvent.id", target = "lunchEventId")
    @Mapping(source = "lunchEvent.restaurant.name", target = "restaurant")
    @Mapping(source = "lunchEvent.date", target = "date")
    @Mapping(source = "lunchEvent.time", target = "time")
    RegisterInfo toRegisterInfo(Register register);


  List <RegisterInfo> toRegisterInfos(List <Register> registers);


}