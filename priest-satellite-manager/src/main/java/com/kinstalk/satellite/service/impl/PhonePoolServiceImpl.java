package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.dao.PhonePoolDao;
import com.kinstalk.satellite.domain.PhonePool;
import com.kinstalk.satellite.service.api.PhonePoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/11.
 */

@Service
public class PhonePoolServiceImpl implements PhonePoolService{


    @Resource
    private PhonePoolDao phonePoolDao;

    @Override
    public PhonePool selectOnePool(){
       return phonePoolDao.selectSingle();
    }

    @Override
    public List<PhonePool> selectList(){
       return phonePoolDao.selectList();
    }
}
