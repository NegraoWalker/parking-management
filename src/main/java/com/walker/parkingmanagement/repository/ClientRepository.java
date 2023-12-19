package com.walker.parkingmanagement.repository;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client,Long> {
    @Query("select c from Client c")
    Page<ClientProjection> findAllPageable(Pageable pageable);

    Client findByUserId(Long id);
}
