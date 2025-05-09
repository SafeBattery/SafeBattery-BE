package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.capstone.safebattery.domain.Client;
import sejong.capstone.safebattery.domain.Pemfc;

import java.util.List;

public interface PemfcRepository extends JpaRepository<Pemfc, Long> {
    //save
    //findById
    //findAll
    //delete
    List<Pemfc> findAllByClient(Client client);
}
