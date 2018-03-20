package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Iterables;
import com.kinstalk.satellite.dao.LogApiDao;
import com.kinstalk.satellite.domain.LogApi;
import com.kinstalk.satellite.domain.ReportDayApi;
import com.kinstalk.satellite.domain.ReportLastApi;
import com.kinstalk.satellite.domain.ReportLastHoursApi;
import com.kinstalk.satellite.service.api.LogApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午5:55
 */
@Service
public class LogApiServiceImpl implements LogApiService {

    private static Logger LOGGER = LoggerFactory.getLogger(LogApiServiceImpl.class);


    @Resource
    private LogApiDao logApiDao;

    @Value("${batch.insert.size}")
    private int batchSize;

    @Override
    public boolean save(LogApi logApi) {
        logApiDao.insertLogApi(logApi);
        return true;
    }

    @Override
    public boolean batchInsertLogApi(List<LogApi> apis) {
        Iterable<List<LogApi>> subSets = Iterables.partition(apis, batchSize);
        for (List<LogApi> list : subSets) {
            if (list == null || list.size() == 0) {
                continue;
            }
            int row = logApiDao.batchInsertLogApi(list);
            if (row == 0) {
                return false;
            }
        }
        return true;

    }

    @Override
    public List<ReportLastApi> batchFindByTime(Long createTime) {
        return logApiDao.batchFindByTime(createTime);
    }

    @Override
    public List<ReportLastHoursApi> batchFindLastHoursByTime(Long createTime, Long updateTime) {
        List<ReportLastHoursApi> reportLastHoursApis = logApiDao.batchFindLastHoursByTime(createTime);
        if (reportLastHoursApis == null || reportLastHoursApis.size() == 0) {
            return null;
        }
        for (ReportLastHoursApi reportLastHoursApi : reportLastHoursApis) {
            reportLastHoursApi.setCreateTime(updateTime);
            reportLastHoursApi.setUpdateTime(updateTime);
        }
        return reportLastHoursApis;
    }

    @Override
    public List<ReportDayApi> batchFindDaysByTime(Long startTime, Long endTime, Long updateTime) {
        List<ReportDayApi> reportDayApis = logApiDao.batchFindDaysByTime(startTime, endTime);
        if (reportDayApis == null || reportDayApis.size() == 0) {
            return null;
        }
        for (ReportDayApi reportDayApi : reportDayApis) {
            reportDayApi.setCreateTime(updateTime);
            reportDayApi.setUpdateTime(updateTime);
        }

        return reportDayApis;
    }

}
