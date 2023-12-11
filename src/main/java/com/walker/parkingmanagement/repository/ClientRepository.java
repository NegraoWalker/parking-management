package com.walker.parkingmanagement.repository;

import com.walker.parkingmanagement.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
