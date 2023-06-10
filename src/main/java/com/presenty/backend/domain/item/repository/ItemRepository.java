package com.presenty.backend.domain.item.repository;

import com.presenty.backend.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
