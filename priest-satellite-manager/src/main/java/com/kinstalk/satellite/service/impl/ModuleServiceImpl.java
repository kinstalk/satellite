package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Iterables;
import com.kinstalk.satellite.dao.ModuleDao;
import com.kinstalk.satellite.domain.Module;
import com.kinstalk.satellite.service.api.ModuleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午5:45
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Resource
    private ModuleDao moduleDao;

    @Value("${batch.insert.size}")
    private int batchSize;

    @Override
    public Module getById(Long moduleId) {
        return moduleDao.getById(moduleId);
    }

    @Override
    public boolean save(Module module) {
        moduleDao.insertModule(module);
        return true;
    }

    @Override
    public boolean batchInsertModule(List<Module> modules) {
        Iterable<List<Module>> subSets = Iterables.partition(modules, batchSize);
        for (List<Module> list : subSets) {
            if (list == null || list.size() == 0) {
                continue;
            }
            int row = moduleDao.batchInsertModule(list);
            if (row == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public List<Module> batchFindByTime(List<Long> times) {
        if (times == null || times.size() == 0) {
            return null;
        }
        return moduleDao.batchFindByTime(times);
    }
}
