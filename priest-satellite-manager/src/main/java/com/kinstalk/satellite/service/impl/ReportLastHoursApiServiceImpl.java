package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Iterables;
import com.kinstalk.satellite.dao.ReportLastHoursApiDao;
import com.kinstalk.satellite.domain.ReportLastHoursApi;
import com.kinstalk.satellite.service.api.ReportLastHoursApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:42
 */
@Service
public class ReportLastHoursApiServiceImpl implements ReportLastHoursApiService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportLastHoursApiServiceImpl.class);

    @Resource
    private ReportLastHoursApiDao reportLastHoursApiDao;

    @Value("${batch.insert.size}")
    private int batchSize;

    @Override
    public boolean insert(ReportLastHoursApi reportLastHoursApi) {
        int row = reportLastHoursApiDao.insert(reportLastHoursApi);
        //LOGGER.info("insert reportLastHoursApi,reportLastHoursApi:{},row:{}", reportLastHoursApi.toString(), row);
        return true;
    }

    @Override
    public List<ReportLastHoursApi> findByApiId(Long apiId) {
        return reportLastHoursApiDao.getByApiId(apiId);
    }

    @Override
    public boolean batchInsertReportLastHour(List<ReportLastHoursApi> reportLastHoursApis) {
        Iterable<List<ReportLastHoursApi>> sets = Iterables.partition(reportLastHoursApis, batchSize);
        for (List<ReportLastHoursApi> list : sets) {
            if (list == null || list.size() == 0) {
                continue;
            }
            int row = reportLastHoursApiDao.batchInsertReportLastHour(list);
            if (row == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean batchDeleteLastHours(Long time) {
        int row = reportLastHoursApiDao.batchDeleteLastHours(time);
        LOGGER.info("batch delete last hours,time:{},row:{}", time, row);
        return true;
    }
}
