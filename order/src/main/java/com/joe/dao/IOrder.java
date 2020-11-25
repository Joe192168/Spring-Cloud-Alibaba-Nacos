package com.joe.dao;

import com.joe.entity.Order;

public interface IOrder {

    int save(Order order);

    int update(Order order);

}
