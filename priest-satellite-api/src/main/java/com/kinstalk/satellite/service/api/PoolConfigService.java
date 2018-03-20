package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.AgentStatus;
import com.kinstalk.satellite.domain.PhonePool;
import com.kinstalk.satellite.domain.PoolConfig;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/7/8.
 */
public interface PoolConfigService {

    public List<PhonePool> selectOneConfig();

    public long delete(Long id);

    public long save(PoolConfig poolConfig);

    public long savePool(PhonePool phonePool);

    public long update(PoolConfig poolConfig);

    public PoolConfig selectRemain();

    public String selectMax();

    public long deletePool();

}
