package jks.lototronback.service.register;

import jks.lototronback.controller.register.RegisterInfo;
import jks.lototronback.persistence.register.Register;
import jks.lototronback.persistence.register.RegisterMapper;
import jks.lototronback.persistence.register.RegisterRepository;
import jks.lototronback.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationsService {

    private final RegisterRepository registerRepository;
    private final RegisterMapper registerMapper;

    public List<RegisterInfo> findUserRegistrations(int userId) {
        List<Register> registers = registerRepository.findRegistersFromDate(userId, LocalDate.now(), Status.ACTIVE.getCode());
        List<RegisterInfo> registerInfos = registerMapper.toRegisterInfos(registers);
        for (RegisterInfo registerInfo : registerInfos) {
            registerInfo.setPaxRegistered(registerRepository.countRegisteredParticipants(registerInfo.getLunchEventId(), Status.ACTIVE.getCode()));

        }

        return registerInfos;
    }
}
