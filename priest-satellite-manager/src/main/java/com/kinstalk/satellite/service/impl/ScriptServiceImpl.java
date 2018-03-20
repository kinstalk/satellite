package com.kinstalk.satellite.service.impl;


import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.dao.RunDao;
import com.kinstalk.satellite.dao.ScriptMapper;
import com.kinstalk.satellite.domain.RunScriptTimer;
import com.kinstalk.satellite.domain.Script;
import com.kinstalk.satellite.service.api.ScriptService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchuanqi on 16/4/14.
 */
@Service
public class ScriptServiceImpl implements ScriptService{




    private Logger logger= LoggerFactory.getLogger(ScriptServiceImpl.class);
    @Resource
    private  ScriptMapper scriptMapper;

    @Resource
    private RunDao runDao;

    @Override
    public List<Script> queryScriptPage() {
        List<Script> list = null;


        try {

           list= scriptMapper.selectList();

        } catch (Exception e) {
          e.getMessage();

        }
        return list;
    }
    @Override
    public Script queryScript(Long id) {
        Script script= null;
        try {

            script = scriptMapper.selectById(id);

        } catch (Exception e) {
            e.getMessage();

        }
        return script;
    }
    @Override
    public long saveScript(Script script) {
        long rows = 0;
        try {
            if (script != null) {
                Long currentTime = System.currentTimeMillis();
                script.setCreateTime(currentTime);
                if (script.getId() != null && script.getId() != 0) {
                    //更新
                    rows = scriptMapper.update(script);
                } else {
                    //插入
                    rows = scriptMapper.save(script);
                }
            }
        } catch (Exception e) {
            e.getMessage();

            logger.error(e.getMessage(),e);

        }
        return rows;
    }
    @Override
    public long deleteScript(Long id) {
        long rows = 0;
        try {

            rows = scriptMapper.delete(id);

        } catch (Exception e) {
           e.getMessage();

        }
        return rows;
    }

    public List<RunScriptTimer> findRuntierScript(Long id){



        return runDao.selectByScriptId(id);
    }
}
