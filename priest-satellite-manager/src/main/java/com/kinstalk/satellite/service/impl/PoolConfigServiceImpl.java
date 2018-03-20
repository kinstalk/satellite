package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.dao.ModuleDao;
import com.kinstalk.satellite.dao.PhonePoolDao;
import com.kinstalk.satellite.dao.PoolConfigDao;
import com.kinstalk.satellite.domain.PhonePool;
import com.kinstalk.satellite.domain.PoolConfig;
import com.kinstalk.satellite.service.api.PoolConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/8.
 */

@Service
public class PoolConfigServiceImpl implements PoolConfigService {



    @Resource
    private PoolConfigDao poolConfigDao;

    @Resource
    private PhonePoolDao phonePoolDao;


    @Override
    public List<PhonePool> selectOneConfig(){

        return poolConfigDao.selectList();
    }

    @Override
    public PoolConfig selectRemain(){

        return poolConfigDao.selectRemain();
    }

    @Override
    public String selectMax(){
        return poolConfigDao.selectMax();
    }

    @Override
    public long delete(Long id){
        return poolConfigDao.delete(id);
    }

    @Override
    public long save(PoolConfig poolConfig){
        return  poolConfigDao.save(poolConfig);
    }

    @Override
    public long update(PoolConfig poolConfig) {
        return  poolConfigDao.update(poolConfig);
    }

    @Override
    public long savePool(PhonePool phonePool){
        return  phonePoolDao.save(phonePool);
    }

    @Override
    public long deletePool(){
        return phonePoolDao.delete();
    }


}
