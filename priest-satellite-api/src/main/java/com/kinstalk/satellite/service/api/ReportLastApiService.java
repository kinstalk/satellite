package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.Module;
import com.kinstalk.satellite.domain.ReportLastApi;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:36
 */
public interface ReportLastApiService {

    /**
     * 保存接口最新报表信息
     *
     * @param reportLastApiDTO 报表信息
     * @return 是否保存成功
     */
    boolean insert(ReportLastApi reportLastApiDTO);

    /**
     * 查询接口最新报表信息
     *
     * @param moduleId
     * @return 报表信息列表
     */
    List<ReportLastApi> findAll(Long moduleId,Integer appId);

    /**
     * 查询接口最新报表信息
     * @return 报表信息列表
     */
    List<ReportLastApi> findFailList(Integer appId);


    /**
     * 批量插入
     *
     * @param reportLastApiDTOs 批量列表信息
     * @return 是否插入成功
     */
    boolean batchInsertLogApi(List<ReportLastApi> reportLastApiDTOs);

    /**
     * 批量删除
     *
     * @param time
     * @return
     */
    boolean batchDeleteLogApi(Long time,Integer agentId,Integer appId,Long timerId);

    /**
     * 流水表和最新表联系查询及插入
     *
     * @return 是否插入成功
     */
    boolean batchJoinInsert(Long time,Integer agentId,Long timerId);


    /**
     * 批量更新最新表成功率
     *
     * @param time 时间
     * @return 影响的行数
     */
    boolean batchUpdateJoin(Long time,Long timerId);

    /**
     * 查询模块状态
     *
     * @return 按模块返回
     */
    List<Module> batchFindModule(Integer appId);


    /**
     * 批量插入module报表
     * @return
     */
    boolean batchInserReportModule(Integer agentId);

    /**
     * 删除module报表
     */

    boolean deleteReportModule(Integer agentId);

    /**
     * 更新module表
     */

    boolean updateReportModule(Integer agentId);
    /**
     * 批量插入week_api
     * @return
     */
    boolean batchInsertWeekApi(Integer appId);


}
