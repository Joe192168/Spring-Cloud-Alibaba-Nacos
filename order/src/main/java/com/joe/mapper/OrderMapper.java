package com.joe.mapper;

import com.joe.entity.Order;

public interface OrderMapper {

    int save(Order order);

    int update(Order order);

}
