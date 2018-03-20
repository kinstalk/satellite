package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.domain.ReportLastHoursApi;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:41
 */
public interface ReportLastHoursApiService {

    /**
     * 保存接口24小时报表信息
     *
     * @param reportLastHoursApiDTO 报表信息对象
     * @return 是否保存成功
     */
    boolean insert(ReportLastHoursApi reportLastHoursApiDTO);

    /**
     * 根据接口id查询近24小时接口信息
     *
     * @param apiId 接口id
     * @return 信息对象
     */
    List<ReportLastHoursApi> findByApiId(Long apiId);

    /**
     * 批量插入
     *
     * @param reportLastHoursApis 批量列表
     * @return 接口详细
     */
    boolean batchInsertReportLastHour(List<ReportLastHoursApi> reportLastHoursApis);

    /**
     * 批量删除
     *
     * @param time 时间点
     * @return 是否删除成功
     */
    boolean batchDeleteLastHours(Long time);

}
