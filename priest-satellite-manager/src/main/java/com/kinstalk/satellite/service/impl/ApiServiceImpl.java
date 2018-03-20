package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Iterables;
import com.kinstalk.satellite.dao.ApiDao;
import com.kinstalk.satellite.domain.Api;
import com.kinstalk.satellite.service.api.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午4:28
 */
@Service
public class ApiServiceImpl implements ApiService {

    private static Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    @Resource
    private ApiDao apiDao;

    @Value("${batch.insert.size}")
    private int batchSize;


    @Override
    public boolean save(Api apiDTO) {
        Api api = new Api();
        BeanUtils.copyProperties(apiDTO, api);
        int row = apiDao.insertApi(api);
        LOGGER.info("save api result,apiUrl:{},row:{}", apiDTO.getApiUrl(), row);
        return true;
    }

    @Override
    public List<Api> findByModuleId(Long moduleId) {
        return apiDao.findByModuleId(moduleId);
    }

    @Override
    public boolean batchInsertApi(List<Api> apiDTOs) {
        Iterable<List<Api>> subSets = Iterables.partition(apiDTOs, batchSize);
        for (List<Api> list : subSets) {
            if (list == null || list.size() == 0) {
                continue;
            }
            int row = apiDao.batchInsertApi(list);
            if (row == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Api> findAll(Integer appId) {
        return apiDao.findAll(appId);
    }

    @Override
    public List<Api> batchFindByTime(List<Long> times) {
        if (times == null || times.size() == 0) {
            return null;
        }

        return apiDao.batchFindByTime(times);
    }

}
