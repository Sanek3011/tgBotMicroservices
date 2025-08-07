package org.example.orderservice.repository;

import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.status NOT IN :excludedStatuses ORDER BY o.date DESC")
    Page<Order> findActiveOrders(
            @Param("excludedStatuses") Set<Status> excludedStatuses,
            Pageable pageable
    );
}
