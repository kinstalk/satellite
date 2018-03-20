package com.kinstalk.satellite.service.api;

import com.kinstalk.satellite.domain.ReportDayApi;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:01
 */
public interface ReportDayApiService {

    /**
     * 保存一天接口调用信息
     *
     * @param reportDayApiDTO 接口信息对象
     * @return 是否保存成功
     */
    boolean insert(ReportDayApi reportDayApiDTO);

    /**
     * 查询某个时间段所有接口报表信息
     *
     * @param time 查询时间
     * @return 接口报表信息列表
     */
    List<ReportDayApi> findByWeek(Long time,Integer appId);

    /**
     * 查询接口详情
     *
     * @param startTime 开始时间
     * @param apiId 接口id
     * @return 返回列表
     */
    List<ReportDayApi> findByTimeAndId(Long startTime, Long apiId);

    /**
     * 批量插入
     *
     * @param reportDayApis
     * @return
     */
    boolean batchInsertReportDay(List<ReportDayApi> reportDayApis);

    /**
     * 批量删除
     *
     * @param time 时间点
     * @return 影响的行数
     */
    boolean batchDeleteLogApi(Long time);






}
