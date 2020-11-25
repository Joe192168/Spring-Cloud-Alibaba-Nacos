package com.joe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * order实体
 */
@Data
@TableName("t_order")
public class Order {

    @TableId
    private int id;
    private String name;
}
