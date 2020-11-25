package com.joe.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * play实体
 */
@Data
@TableName("t_play")
public class Play {

    @TableId
    private int id;
    private String name;

}
