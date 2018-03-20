package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Iterables;
import com.kinstalk.satellite.dao.ReportLastApiDao;
import com.kinstalk.satellite.domain.Module;
import com.kinstalk.satellite.domain.ReportLastApi;
import com.kinstalk.satellite.service.api.ReportLastApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:37
 */
@Service
public class ReportLastApiServiceImpl implements ReportLastApiService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportLastApiServiceImpl.class);

    @Resource
    private ReportLastApiDao reportLastApiDao;

    @Value("${batch.insert.size}")
    private int batchSize;

    @Override
    public boolean insert(ReportLastApi reportLastApi) {
        int row = reportLastApiDao.insert(reportLastApi);
        if (row == 0) {
            return false;
        }
        return true;
    }

    @Override
    public List<ReportLastApi> findAll(Long moduleId,Integer appId) {
        return reportLastApiDao.findAll(moduleId,appId);
    }
    @Override
    public List<ReportLastApi> findFailList(Integer appId) {
        return reportLastApiDao.findFailList(appId);
    }


    @Override
    public boolean batchInsertLogApi(List<ReportLastApi> reportLastApis) {
        Iterable<List<ReportLastApi>> sets = Iterables.partition(reportLastApis, batchSize);
        for (List<ReportLastApi> list : sets) {
            if (list == null || list.size() == 0) {
                continue;
            }
            int row = reportLastApiDao.batchInsertLogApi(list);
            if (row == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean batchDeleteLogApi(Long time,Integer agentId,Integer appId,Long timerId) {
        int row = reportLastApiDao.batchDeleteLogApi(time,agentId,appId,timerId);
        //LOGGER.info("batch join insert,time:{},row:{}", time, row);
        return true;
    }

    @Override
    public boolean batchJoinInsert(Long time,Integer agentId,Long timerId) {
        int row = reportLastApiDao.batchJoinInsert(time,agentId,timerId);
        //LOGGER.info("batchJoinInsert,row:{}", row);
        return true;
    }

    @Override
    public boolean batchUpdateJoin(Long time,Long timerId) {
        int row = reportLastApiDao.batchUpdateJoin(time,timerId);
        //LOGGER.info("batchUpdateJoin,time:{},row:{}", time, row);
        return true;
    }

    @Override
    public boolean batchInserReportModule(Integer key){
        try {
            int row=reportLastApiDao.batchInserReportModule(key);

            return  true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return  false;
        }



    }
    @Override
    public boolean deleteReportModule(Integer agentId){

        try {
            int row=reportLastApiDao.deleteReportModule(agentId);

            return  true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return  false;
        }

    }
    @Override
    public  boolean updateReportModule(Integer agentId){
        try {
            int row=reportLastApiDao.updateReportModule(agentId);

            return  true;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            return  false;
        }
    }
    @Override
    public List<Module> batchFindModule(Integer appId) {
        List<Module> modules = reportLastApiDao.batchFindModule(appId);
        if (modules == null || modules.size() == 0) {
            return null;
        }
        //LOGGER.info("batchFindModule,size:{},result:{}", modules.size(), modules.toString());
        return modules;
    }
    @Override
    public boolean batchInsertWeekApi(Integer appId){
        int row =reportLastApiDao.batchInsertWeekApi(appId);
        if(row==0){
            return false;
        }
        return true;
    }


}
