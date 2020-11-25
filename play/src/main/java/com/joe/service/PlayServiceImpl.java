package com.joe.service;

import com.joe.dao.IPlay;
import com.joe.entity.Play;
import com.joe.mapper.PlayMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PlayServiceImpl implements IPlay {

    @Autowired
    private PlayMapper playMapper;

    /*@Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(){
        log.info("调用play的add方法");
        jdbcTemplate.update("insert into t_play(name) values('张三')");
    }*/

    @Override
    public int save(Play play) {
        log.info("调用play的save方法");
        return playMapper.save(play);
    }

    @Override
    public int update(Play play) {
        log.info("调用play的update方法");
        return playMapper.update(play);
    }
}
