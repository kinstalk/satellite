package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.Module;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午5:44
 */
public interface ModuleService {

    /**
     * 根据模块id查询模块信息
     *
     * @param moduleId 模块id
     * @return 模块信息
     */
    Module getById(Long moduleId);

    /**
     * 保存模块信息
     *
     * @param moduleDTO 模块信息对象
     * @return 是否保存成功
     */
    boolean save(Module moduleDTO);

    /**
     * 批量保存模块信息
     *
     * @param moduleDTOs 模块列表
     * @return 影响的列数
     */
    boolean batchInsertModule(List<Module> moduleDTOs);

    /**
     * 查询所有模块信息
     *
     * @return 模块列表
     */
    List<Module> findAll();

    /**
     * 根据创建时间批量查询模块信息
     *
     * @param times 时间列表
     * @return 模块列表
     */
    List<Module> batchFindByTime(List<Long> times);
}
