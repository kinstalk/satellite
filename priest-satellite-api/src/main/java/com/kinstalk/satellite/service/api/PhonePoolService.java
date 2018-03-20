package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.PhonePool;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/11.
 */
public interface PhonePoolService {

    public PhonePool selectOnePool();

    public List<PhonePool> selectList();
}
