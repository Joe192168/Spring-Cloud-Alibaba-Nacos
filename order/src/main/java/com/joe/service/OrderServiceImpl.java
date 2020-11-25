package com.joe.service;

import com.joe.dao.IOrder;
import com.joe.entity.Order;
import com.joe.mapper.OrderMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OrderServiceImpl implements IOrder {

    @Autowired
    private OrderMapper orderMapper;

    /*@Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(){
        log.info("调用Order的add方法");
        jdbcTemplate.update("insert into t_order(name) values('张三')");
    }*/

    @Override
    public int save(Order order) {
        log.info("调用Order的save方法");
        return orderMapper.save(order);
    }

    @Override
    public int update(Order order) {
        log.info("调用Order的update方法");
        return orderMapper.update(order);
    }
}
