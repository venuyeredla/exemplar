package com.exemplar.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pilot.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


}
