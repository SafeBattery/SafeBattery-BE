package sejong.capstone.safebattery.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sejong.capstone.safebattery.domain.TemperatureDynamask;
import sejong.capstone.safebattery.domain.VoltagePowerDynamask;
import sejong.capstone.safebattery.repository.TemperatureDynamaskRepository;
import sejong.capstone.safebattery.repository.VoltagePowerDynamaskRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamaskService {
    private final VoltagePowerDynamaskRepository vpDynamaskRepository;
    private final TemperatureDynamaskRepository tempDynamaskRepository;

    public VoltagePowerDynamask addNewVoltagePowerDynamask(VoltagePowerDynamask dynamask) {
        return vpDynamaskRepository.save(dynamask);
    }

    public Optional<VoltagePowerDynamask> searchVoltagePowerDynamaskById(Long id) {
        return vpDynamaskRepository.findById(id);
    }

    public TemperatureDynamask addNewTemperatureDynamask(TemperatureDynamask dynamask) {
        return tempDynamaskRepository.save(dynamask);
    }

    public Optional<TemperatureDynamask> searchTemperatureDynamaskById(Long id) {
        return tempDynamaskRepository.findById(id);
    }
}
