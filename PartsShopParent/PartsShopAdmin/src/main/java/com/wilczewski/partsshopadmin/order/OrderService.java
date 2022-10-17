package com.wilczewski.partsshopadmin.order;

import com.wilczewski.partsshopcommon.entity.Order;
import com.wilczewski.partsshopcommon.entity.Product;
import org.springframework.data.domain.Page;

public interface OrderService {

    public Page<Order> listByPage(int pageNumber, String sortField, String sortDir, String keyword);

    public Order get(Integer id) throws OrderNotFoundException;

    public void delete(Integer id) throws OrderNotFoundException;
}
