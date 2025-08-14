package org.example.orderservice.repository;

import org.example.orderservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.SequencedCollection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByDesc(String desc);

    Optional<Item> findByType(String type);
}
