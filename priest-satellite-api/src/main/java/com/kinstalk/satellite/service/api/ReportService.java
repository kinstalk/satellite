package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.domain.*;

import java.util.List;
import java.util.Map;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午3:55
 */
public interface ReportService {

    Map<String, Object> getDetailById(Long apiId);

    /**
     * 批量保存接口数据
     *
     * @param logApiDTOList 接口信息
     * @return 是否保存成功
     */
    boolean batchInsertReport(List<BatchInsertParam> logApiDTOList,Integer sleepTime,Integer agentid,Integer appid,Long timerId);


    /**
     * 获取接口当前状态
     *
     * @param moduleId
     * @return 返回列表
     */
    List<ReportLastApi> findCurrentList(Long moduleId,Integer appId);



    /**
     * 获取接口当前状态
     * @return 返回列表
     */
    List<ReportLastApi> findFailList(Integer appId);


    /**
     * 查询一周的时间
     *
     * @param time 时间点
     * @return 返回列表
     */
    List<ReportWeekResult> findWeekList(long time,Integer appId);

    /**
     * 查询模块状态
     *
     * @return 按模块返回
     */
    List<Module> findModuleList(Integer appId);

    /**
     * 根据接口id查询近24小时接口信息
     *
     * @param apiId    接口id
     * @return 信息对象
     */
    List<ReportLastHoursApi> findByApiId(Long apiId);

    /**
     * 查询最近一周module fail
     * @param reportLastWeekRate
     * @return
     */
    List<ReportLastWeekRate> findModuleFailListWeekRate(ReportLastWeekRate reportLastWeekRate);

    /**
     * 接口统计图查询
     * @param reportLastWeekRate
     * @return
     */
    List<ReportLastWeekRate> findApiFailListWeekRate(ReportLastWeekRate reportLastWeekRate);

}
