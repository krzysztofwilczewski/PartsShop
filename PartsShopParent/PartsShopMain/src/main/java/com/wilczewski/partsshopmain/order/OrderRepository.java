package com.wilczewski.partsshopmain.order;

import com.wilczewski.partsshopcommon.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
