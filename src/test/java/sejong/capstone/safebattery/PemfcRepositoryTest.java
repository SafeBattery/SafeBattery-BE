package sejong.capstone.safebattery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.safebattery.domain.Pemfc;
import sejong.capstone.safebattery.repository.PemfcRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PemfcRepositoryTest {
    @Autowired
    PemfcRepository repository;

    @Test
    public void create() {
        //given
        Pemfc pemfc = new Pemfc(1.047, 0.956, 0.112, 0.107,
                7.749, 1.052, 94.016, 100.098,
        0.96, 0.556, 1.059, 0.677,
                14.691, -64762.981, 34.011, 14.584,
                29.724, 18.663, 42.509,
        64.992, 0.024, 0.002, 0, 0);

        //when
        Pemfc savedRow = repository.save(pemfc);

        //then
        assertThat(savedRow.getTsec()).isEqualTo(1.047);
    }

    @Test
    public void read() {
        //given
        Pemfc pemfc = new Pemfc(1.047, 0.956, 0.112, 0.107,
                7.749, 1.052, 94.016, 100.098,
                0.96, 0.556, 1.059, 0.677,
                14.691, -64762.981, 34.011, 14.584,
                29.724, 18.663, 42.509,
                64.992, 0.024, 0.002, 0, 0);
        Pemfc savedRow = repository.save(pemfc);

        //when
        Optional<Pemfc> findRow = repository.findById(savedRow.getId());
        Optional<Pemfc> noSuchRow = repository.findById(-1L);

        //then
        assertThat(findRow.orElseThrow().getTsec()).isEqualTo(1.047);
        assertThatThrownBy(noSuchRow::orElseThrow).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void readAll() {
        //given
        Pemfc pemfc1 = new Pemfc(1.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0);
        Pemfc pemfc2 = new Pemfc(2.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0);
        Pemfc pemfc3 = new Pemfc(3.047, 0.956, 0.112, 0.107, 7.749, 1.052, 94.016, 100.098, 0.96, 0.556, 1.059, 0.677, 14.691, -64762.981, 34.011, 14.584, 29.724, 18.663, 42.509, 64.992, 0.024, 0.002, 0, 0);

        //when
        repository.save(pemfc1);
        repository.save(pemfc2);
        repository.save(pemfc3);

        //then
        assertThat(repository.findAll().size()).isEqualTo(3);
    }
}
