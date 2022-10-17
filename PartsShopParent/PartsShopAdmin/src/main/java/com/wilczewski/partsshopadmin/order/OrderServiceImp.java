package com.wilczewski.partsshopadmin.order;

import com.wilczewski.partsshopcommon.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderServiceImp implements OrderService{

    public static final int ORDERS_PER_PAGE = 10;

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImp(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {

        Sort sort = null;

        if ("destination".equals(sortField)){
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, ORDERS_PER_PAGE, sort);

        Page<Order> page = null;

        if (keyword != null) {
            page = orderRepository.findAll(keyword, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        return page;
    }

    @Override
    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return orderRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new OrderNotFoundException("Nie znaleiozono zamówienia o ID " + id);
        }
    }

    @Override
    public void delete(Integer id) throws OrderNotFoundException {
        Long count = orderRepository.countById(id);

        if (count == null || count == 0) {
            throw new OrderNotFoundException("Nie znaleziono zamówienia o ID " + id);
        }
        orderRepository.deleteById(id);
    }
}
