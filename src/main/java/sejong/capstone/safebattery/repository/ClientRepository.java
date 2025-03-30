package sejong.capstone.safebattery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.capstone.safebattery.domain.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    //save
    //findById
    //findAll
    //delete
}
