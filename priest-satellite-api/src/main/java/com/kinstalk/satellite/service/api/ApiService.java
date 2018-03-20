package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.domain.Api;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午4:02
 */
public interface ApiService {

    /**
     * 保存接口信息
     *
     * @param Api 接口对象
     * @return 是否保存成功
     */
    boolean save(Api Api);

    /**
     * 根据模块id查询接口列表
     *
     * @param moduleId 模块id
     * @return 接口列表
     */
    List<Api> findByModuleId(Long moduleId);

    /**
     * 批量保存接口信息
     *
     * @param apis 接口列表
     * @return 是否操作成功
     */
    boolean batchInsertApi(List<Api> apis);

    /**
     * 查询所有接口列表
     *
     * @return 接口列表
     */
    List<Api> findAll(Integer appId);

    /**
     * 根据创建时间查询接口信息
     *
     * @param times 时间列表
     * @return 接口列表
     */
    List<Api> batchFindByTime(List<Long> times);
}
