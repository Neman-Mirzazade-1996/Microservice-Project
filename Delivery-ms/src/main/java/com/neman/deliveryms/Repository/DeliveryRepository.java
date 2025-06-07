package com.neman.deliveryms.Repository;

import com.neman.deliveryms.Model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Custom query methods can be defined here if needed
}
