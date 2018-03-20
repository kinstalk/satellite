package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.domain.LogApi;
import com.kinstalk.satellite.domain.ReportDayApi;
import com.kinstalk.satellite.domain.ReportLastApi;
import com.kinstalk.satellite.domain.ReportLastHoursApi;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午5:54
 */
public interface LogApiService {

    /**
     * 保存流水表记录
     *
     * @param logApiDTO 流水信息对象
     * @return 保存是否成功
     */
    boolean save(LogApi logApiDTO);

    /**
     * 批量插入流水表记录
     *
     * @param apis 批量信息
     * @return 是否插入成功
     */
    boolean batchInsertLogApi(List<LogApi> apis);


    /**
     * 根据时间查询一批接口的情况
     *
     * @param createTime 时间
     * @return 接口列表
     */
    List<ReportLastApi> batchFindByTime(Long createTime);

    /**
     * 根据时间查询过去24小时详细状况
     *
     * @param createTime 查询时间
     * @param updateTime 入库的时间
     * @return 接口详情列表
     */
    List<ReportLastHoursApi> batchFindLastHoursByTime(Long createTime, Long updateTime);

    /**
     * 某时间段内接口状况
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param updateTime 入库时间
     * @return
     */
    List<ReportDayApi> batchFindDaysByTime(Long startTime, Long endTime, Long updateTime);
}
