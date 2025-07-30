package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByCityContainingIgnoreCase(String city);
    List<Destination> findByUser(User user);
    List<Destination> findByCountryContainingIgnoreCase(String country);
    List<Destination> findByCityContainingIgnoreCaseAndCountryContainingIgnoreCase(String city, String country);
}
