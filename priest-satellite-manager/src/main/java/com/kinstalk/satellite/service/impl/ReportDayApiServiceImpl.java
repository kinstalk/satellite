package com.kinstalk.satellite.service.impl;

import com.kinstalk.satellite.common.utils.DateUtil;
import com.kinstalk.satellite.dao.ReportDayApiDao;
import com.kinstalk.satellite.domain.ReportDayApi;
import com.kinstalk.satellite.service.api.ReportDayApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:06
 */
@Service
public class ReportDayApiServiceImpl implements ReportDayApiService {

    @Resource
    private ReportDayApiDao reportDayApiDao;

    @Override
    public boolean insert(ReportDayApi reportDayApi) {
        reportDayApiDao.insert(reportDayApi);
        return true;
    }

    @Override
    public List<ReportDayApi> findByWeek(Long startTime,Integer appId) {
        String endTime = DateUtil.addDay(startTime+"", 6);
        return reportDayApiDao.findByTime(startTime, Long.parseLong(endTime),appId);
    }

    @Override
    public List<ReportDayApi> findByTimeAndId(Long startTime, Long apiId) {
        return reportDayApiDao.findByTimeAndId(startTime, apiId);
    }

    @Override
    public boolean batchInsertReportDay(List<ReportDayApi> reportDayApis) {
        if (reportDayApis == null || reportDayApis.size() == 0) {
            return false;
        }
        int row = reportDayApiDao.batchInsertReportDay(reportDayApis);
        if (row > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean batchDeleteLogApi(Long time) {
        int row = reportDayApiDao.batchDeleteLogApi(time);
        if (row == 0) {
            return false;
        }
        return true;
    }

}
