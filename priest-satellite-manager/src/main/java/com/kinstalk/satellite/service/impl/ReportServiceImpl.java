package com.kinstalk.satellite.service.impl;

import com.google.common.collect.Lists;
import com.kinstalk.satellite.common.constant.CommonConstant;
import com.kinstalk.satellite.common.utils.DateUtil;
import com.kinstalk.satellite.dao.ReportLastApiDao;
import com.kinstalk.satellite.domain.*;
import com.kinstalk.satellite.service.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: liuling
 * Date: 16/4/13
 * Time: 下午4:04
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);


    @Resource
    private ReportDayApiService reportDayApiService;

    @Resource
    private ReportLastHoursApiService reportLastHoursApiService;

    @Resource
    private ReportLastApiService reportLastApiService;

    @Resource
    private ApiService apiService;

    @Resource
    private ModuleService moduleService;

    @Resource
    private ReportLastApiDao reportLastApiDao;

    @Resource
    private LogApiService logApiService;

    private static Map<Integer,Map<String, Api>> apiMaps = null;

    private static Map<Long, String> apiIdMaps = null;

    private static Map<Integer,Map<String, Module>> modulesMaps = null;

    private static Map<Long, String> moduleIdMaps = null;


    @Override
    public Map<String, Object> getDetailById(Long apiId) {
        Long startTime = System.currentTimeMillis() - CommonConstant.SERVEN_DAY_MS;
        List<ReportDayApi> reportDayApiList = reportDayApiService.findByTimeAndId(startTime, apiId);
        Map<String, Object> maps = new HashMap<>();
        maps.put("lastWeekRate", reportDayApiList);
//        ReportLastHoursApi reportLastHoursApi = reportLastHoursApiService.findByApiId(apiId);
//        maps.put("lastHoursRate", reportLastHoursApi);
        return maps;
    }

    private void initApiMap(Integer appId) {
        //set the api map
        Map<String,Api> mapApi=new HashMap<String,Api>();
        if (apiMaps == null) {
            apiMaps = new HashMap<>();
            apiIdMaps = new HashMap<>();
        }
        apiMaps.put(appId,mapApi);
        if (apiMaps.get(appId).size() > 0) {
            return;
        }
        List<Api> apiDTOList = apiService.findAll(appId);
        if (apiDTOList == null || apiDTOList.size() == 0) {
            return;
        }

        for (Api api : apiDTOList) {
            String apiUrl = api.getApiUrl();
            Long apiId = api.getApiId();
            apiMaps.get(appId).put(apiUrl, api);
            apiIdMaps.put(apiId, apiUrl);
        }

    }

    private void initModuleMap(Integer appId) {
        Map<String,Module> mapModule=new HashMap<String,Module>();
        if (modulesMaps == null) {
            modulesMaps = new HashMap<>();
            moduleIdMaps = new HashMap<>();
        }
        modulesMaps.put(appId,mapModule);
        if (modulesMaps.get(appId).size() > 0) {
            return;
        }
        List<Module> moduleDTOList = moduleService.findAll();
        if (moduleDTOList == null || moduleDTOList.size() == 0) {
            return;
        }

        for (Module moduleDTO : moduleDTOList) {
            String moduleUrl = moduleDTO.getModuleUrl();
            Long moduleId = moduleDTO.getModuleId();


            modulesMaps.get(appId).put(moduleUrl, moduleDTO);
            moduleIdMaps.put(moduleId, moduleUrl);
        }
        modulesMaps.put(appId,mapModule);
    }

    private boolean batchInsertModule(List<Module> moduleDTOs, List<Long> tmpTimeList, Map<String, Api> map, List<Api> apiDTOList,Integer appId) {
        //new module and new api
        if (moduleDTOs.size() > 0) {
            //db:batch insert module
            boolean moduleResult = moduleService.batchInsertModule(moduleDTOs);
            if (!moduleResult) {
                LOGGER.error("batch insert error,moduleDTOs:{}", moduleDTOs.toString());
                return false;
            }
            //batch find module with module_id by createTime
            List<Module> moduleDTOList = moduleService.batchFindByTime(tmpTimeList);
            if (moduleDTOList == null || moduleDTOList.size() == 0) {

                return false;
            }
            //recreate batch insert api list
            for (Module moduleDTO : moduleDTOList) {
//                Long createTime = moduleDTO.getCreateTime();
//                Api apiDTO = map.get(createTime);
//                apiDTO.setCreateTime(System.currentTimeMillis());
//                apiDTO.setModuleId(moduleDTO.getModuleId());


                //update module map
                modulesMaps.get(appId).put(moduleDTO.getModuleUrl(), moduleDTO);
                moduleIdMaps.put(moduleDTO.getModuleId(), moduleDTO.getModuleUrl());
            }

            for (Map.Entry<String, Api> entry : map.entrySet()) {
                Api apiDTO=  entry.getValue();
                for (Module moduleDTO : moduleDTOList) {
                    if(apiDTO.getApiUrl().startsWith(moduleDTO.getModuleUrl())){
                        apiDTO.setModuleId(moduleDTO.getModuleId());
                        break;
                    }

                }

                apiDTO.setCreateTime(System.currentTimeMillis());
                apiDTOList.add(apiDTO);

            }

        }
        return true;
    }

    private boolean batchInsertApi(List<Api> apiDTOList,Integer appId) {
        if (apiDTOList.size() > 0) {
            //db:batch insert api list
            boolean apiResult = apiService.batchInsertApi(apiDTOList);
            if (!apiResult) {
                LOGGER.error("batch insert api error,apiDTOList:{}", apiDTOList.toString());
                return false;
            }
            List<Long> times = new ArrayList<>();
            for (Api apiDTO : apiDTOList) {
                times.add(apiDTO.getCreateTime());
            }
            List<Api> listResult = apiService.batchFindByTime(times);
            if (listResult == null || listResult.size() == 0) {
                return false;
            }

            //update api map
            for (Api apiDTO : listResult) {
                apiMaps.get(appId).put(apiDTO.getApiUrl(), apiDTO);
                apiIdMaps.put(apiDTO.getApiId(), apiDTO.getApiUrl());
            }
        }
        return true;
    }

    private void generateLogApiList(List<BatchInsertParam> subBatchInsertParams, List<LogApi> logApis, Long time,Integer agentId,Integer appId,Long timerId) {
        for (BatchInsertParam batchInsertParam : subBatchInsertParams) {
            String apiUrl = batchInsertParam.getApiUrl();

            if(apiUrl.contains("download")) {
                apiUrl="/download/download";
            }
            Api apiDTO = apiMaps.get(appId).get(apiUrl);
            if (apiDTO == null) {
                continue;
            }

            Long apiId = apiDTO.getApiId();
            //create batch insert log_api list

            Long moduleId = apiDTO.getModuleId();
            LogApi logApi = new LogApi();
            logApi.setApiId(apiId);
            logApi.setModuleId(moduleId);
            logApi.setCostTime(batchInsertParam.getCostTime());
            logApi.setStatus(batchInsertParam.getStatus());
            logApi.setCreateTime(time);
            logApi.setAgentId(agentId);
            logApi.setAppId(appId);
            logApi.setTimerId(timerId);
            logApis.add(logApi);
        }
    }

    private void createLists(Long time, List<BatchInsertParam> subBatchInsertParams, List<LogApi> logApis, List<BatchInsertParam> logApiList, List<Module> moduleDTOs, List<Long> tmpTimeList, Map<String, Api> map, List<Api> apiDTOList,Integer agentId,Integer appid,Long timerId) {
        for (BatchInsertParam batchInsertParam : logApiList) {

            //check the api exist or not according to the #apiMaps#
            String apiUrl = batchInsertParam.getApiUrl();


            if(apiUrl.contains("download")) {
                apiUrl="/download/download";
            }
            Api apiDTO = apiMaps.get(appid).get(apiUrl);

            //not exist, add to #moduleDTOs# list and batch insert
            if (apiDTO == null) {
                //check exist new module or not
                Matcher matcher = Pattern.compile("^(/[^/]+)/").matcher(apiUrl);
                String moduleUrl = null;
                if (matcher.find()) {
                    moduleUrl = matcher.group(1);
                }
                Module moduleDTO = modulesMaps.get(appid).get(moduleUrl);
                //new api and new module
                if (moduleDTO == null) {
                    Long createTime = System.currentTimeMillis();
                    Module moduleDTONew = new Module(moduleUrl, createTime,appid);
                    moduleDTOs.add(moduleDTONew);
                    tmpTimeList.add(createTime);
                    //create tmp map
                    Api apiDTONew = new Api(apiUrl, null, createTime,appid);
                    map.put(apiDTONew.getApiUrl(), apiDTONew);
                }
                //new api and old module
                else {
                    Long createTime = System.currentTimeMillis();
                    Api apiDTONew = new Api(apiUrl, moduleDTO.getModuleId(), createTime,appid);
                    apiDTOList.add(apiDTONew);

                }

                //create sub params list
                subBatchInsertParams.add(batchInsertParam);
            } else {
                //create log_api list
                LogApi logApi = new LogApi(apiDTO.getApiId(), batchInsertParam.getStatus(), batchInsertParam.getCostTime(),time,agentId,appid,timerId);
                logApi.setModuleId(apiDTO.getModuleId());
                logApis.add(logApi);
            }

        }
    }

    @Override
    public boolean batchInsertReport(List<BatchInsertParam> logApiList,Integer sleepTime,Integer agentId,Integer appid,Long timerId) {

        if (logApiList == null || logApiList.size() == 0) {
            LOGGER.info("batch insert report params empty.");
            return false;
        }

        //set the api map
        initApiMap(appid);

        //set the module map
        initModuleMap(appid);

        //new module list:define batch-insert list of module
        List<Module> moduleDTOs = new ArrayList<>();
        //old module and new api:list
        List<Api> apiDTOList = new ArrayList<>();
        //batch insert log_api list
        List<LogApi> logApis = new ArrayList<>();
        //tmp log_api list
        List<BatchInsertParam> subBatchInsertParams = new ArrayList<>();
        //tmp select list
        List<Long> tmpTimeList = new ArrayList<>();
        //tmp map
        Map<String, Api> map = new LinkedHashMap<>();
        //the same batch the same time : log_api
        Long time = System.currentTimeMillis();
        //batch insert . partition into #round# lists
        createLists(time, subBatchInsertParams, logApis, logApiList, moduleDTOs, tmpTimeList, map, apiDTOList,agentId,appid,timerId);

        //new module and new api
        boolean moduleResult = batchInsertModule(moduleDTOs, tmpTimeList, map, apiDTOList,appid);
        if (!moduleResult) {
            LOGGER.info("batchInsertModule error,params is moduleDTOs:{},tmpTimeList:{},map:{},apiDTOList:{}", moduleDTOs.size(), tmpTimeList.size(), map.size(), apiDTOList.size());
            return false;
        }

        //old module and new api
        boolean apiResult = batchInsertApi(apiDTOList,appid);
        if (!apiResult) {
            LOGGER.error("batchInsertApi error,params is apiDTOList:{}", apiDTOList.size());
            return false;
        }

        //batch find api list
        generateLogApiList(subBatchInsertParams, logApis, time,agentId,appid,timerId);

        //batch insert log_api
        boolean logResult = logApiService.batchInsertLogApi(logApis);
        if (!logResult) {
            LOGGER.info("batchInsertLogApi error,params is logApis:{}", logApis.size());
            return false;
        }
        //batch insert report_last_api without last_hours_rate
        boolean joinResult = reportLastApiService.batchJoinInsert(time,agentId,timerId);
        if (!joinResult) {
            LOGGER.info("batchJoinInsert error,no params.");
            return false;
        }
        //batch delete old data of report_last_api
        boolean deleteResult = reportLastApiService.batchDeleteLogApi(time,agentId,appid,timerId);
        if (!deleteResult) {
            LOGGER.info("batchDeleteLogApi error,params is time:{}", time);
            return false;
        }

        //batch update succ_rate of report_last_api
        Long createTime = System.currentTimeMillis() - CommonConstant.TWENTY_FOUR_HOURS;
        boolean updateJoinResult = reportLastApiService.batchUpdateJoin(createTime,timerId);
        if (!updateJoinResult) {
            LOGGER.info("batchUpdateJoin error,params is createTime:{}", createTime);
            return false;
        }
        //batch insert report_last_week_api
        boolean weekReslut=reportLastApiService.batchInsertWeekApi(appid);

        Long updateTime = System.currentTimeMillis();
        List<ReportLastHoursApi> reportLastHoursApis = logApiService.batchFindLastHoursByTime(createTime, updateTime);
        if (reportLastHoursApis == null || reportLastHoursApis.size() == 0) {
            LOGGER.info("batchFindLastHoursByTime error,params is createTime:{}", createTime);
            return false;
        }

        boolean lastResult = reportLastHoursApiService.batchInsertReportLastHour(reportLastHoursApis);
        if (!lastResult) {
            LOGGER.error("batchInsertReportLastHour error,params is reportLastHoursApis:{}", reportLastHoursApis.size());
            return false;
        }

        boolean delete = reportLastHoursApiService.batchDeleteLastHours(updateTime);
        if (!delete) {
            LOGGER.info("batchDeleteLastHours error,params is updateTime:{}", updateTime);
            return false;
        }


        //if it does't collect today,then collect.

        //yesterday:start time
        Long startTime = DateUtil.getTodayStartTime();
        //yesterday:end time
        Long endTime = DateUtil.getTodayEndTime();
        //yesterday date
        Long insertTime = DateUtil.getTodayDate();
        List<ReportDayApi> reportDayApiList = logApiService.batchFindDaysByTime(startTime, endTime, insertTime);
        if (reportDayApiList == null || reportDayApiList.size() == 0) {
            LOGGER.info("batchFindDaysByTime result true,params is startTime:{},endTime:{},insertTime:{}", startTime, endTime, insertTime);
            return true;
        }
        boolean dayResult = reportDayApiService.batchInsertReportDay(reportDayApiList);
        if (!dayResult) {
            LOGGER.error("batchInsertReportDay error,params is reportDayApiList:{}", reportDayApiList.size());
            return false;
        }

        //入module报表



        //插入最新的统计结果
        boolean moduleReprotResult =reportLastApiService.batchInserReportModule(agentId);
        if(!moduleReprotResult){
            LOGGER.info("batchInserReportModule error");
            return false;
        }

        //把最新的统计结果的update_key更新成1
        boolean moduleUpdateResult2 = reportLastApiService.updateReportModule(agentId);

        if(!moduleUpdateResult2){
            LOGGER.info("deleteReportModule");
            return  false;
        }

        //删除原有数据
        boolean moduleDeleteResult = reportLastApiService.deleteReportModule(agentId);
        if(!moduleDeleteResult){
            LOGGER.info("deleteReportModule");

            return  false;
        }





        return true;
    }

    @Override
    public List<ReportLastApi> findCurrentList(Long moduleId,Integer appId) {
        List<ReportLastApi> reportLastApis = reportLastApiService.findAll(moduleId,appId);
        if (reportLastApis == null || reportLastApis.size() == 0) {
            return null;
        }
        initApiMap(appId);
        for (ReportLastApi reportLastApiDetail : reportLastApis) {
            Long apiId = reportLastApiDetail.getApiId();
            reportLastApiDetail.setApiUrl(apiIdMaps.get(apiId));
        }

        // 按URL排序
        Collections.sort(reportLastApis, new Comparator<ReportLastApi>() {
            public int compare(ReportLastApi o1, ReportLastApi o2) {
                return o1.getApiUrl().compareTo(o2.getApiUrl());
            }
        });
        return reportLastApis;
    }

    @Override
    public List<ReportLastApi> findFailList(Integer appId) {
        List<ReportLastApi> reportLastApis = reportLastApiService.findFailList(appId);

        if(reportLastApis!=null&&reportLastApis.size()>0){
            initApiMap(appId);
            for (ReportLastApi reportLastApiDetail : reportLastApis) {
                Long apiId = reportLastApiDetail.getApiId();
                reportLastApiDetail.setApiUrl(apiIdMaps.get(apiId));
            }
            return reportLastApis;
        }else {
            return null;
        }

    }


    @Override
    public List<ReportWeekResult> findWeekList(long time,Integer appId) {
        List<ReportDayApi> reportDayApis = reportDayApiService.findByWeek(time,appId);
        if (reportDayApis == null || reportDayApis.size() == 0) {
            return null;
        }
        initApiMap(appId);
        //Map<String, ReportWeekResult> weekResultMap = new HashMap<>();
        Map<String, ReportWeekResult> weekResultMap = new TreeMap<>();
        for (ReportDayApi reportDayApi : reportDayApis) {
            //set apiUrl
            Long apiId = reportDayApi.getApiId();
            String apiUrl = apiIdMaps.get(apiId);
            reportDayApi.setApiUrl(apiUrl);

            //create model view
            ReportWeekResult weekResult = weekResultMap.get(apiUrl);
            List<Integer> list;
            if (weekResult == null) {
                list = Lists.newArrayList(-1, -1, -1, -1, -1, -1, -1);
            } else {
                list = weekResult.getStatusList();
            }

            Integer status = reportDayApi.getSuccessRate() >= 0.5F ? 1 : 0;
            Long storeTime = reportDayApi.getCreateTime();
            Long maxTime = DateUtil.getBeforeWeekDate(0);
            int index = DateUtil.calculateNumDay(storeTime, maxTime);
            list.set(index, status);
            ReportWeekResult result = new ReportWeekResult(apiId, apiUrl, list,reportDayApi.getAgentArea());
            weekResultMap.put(apiUrl, result);
        }

//        Map<String, ReportWeekResult> sortMap = new TreeMap<String, ReportWeekResult>(new MapKeyComparator());
//        sortMap.putAll(weekResultMap);
        List<ReportWeekResult> weekResults = Lists.newArrayList(weekResultMap.values());

        LOGGER.info("findWeekList result,time:{},weekResults:{}", time, weekResults.toString());
        return weekResults;
    }

//    class MapKeyComparator implements Comparator<String>{
//
//        @Override
//        public int compare(String str1, String str2) {
//
//            return str1.compareTo(str2);
//        }
//    }

    @Override
    public List<Module> findModuleList(Integer appId) {
        List<Module> modules = reportLastApiService.batchFindModule(appId);
        if (modules == null || modules.size() == 0) {
            return null;
        }
//        initModuleMap(appId);
//        for (Module module : modules) {
//            module.setModuleUrl(moduleIdMaps.get(module.getModuleId()));
//        }
        return modules;
    }

    @Override
    public List<ReportLastHoursApi> findByApiId(Long apiId) {
        List<ReportLastHoursApi> reportLastHoursApis = reportLastHoursApiService.findByApiId(apiId);
        if (reportLastHoursApis == null || reportLastHoursApis.size() == 0) {
            return null;
        }
        for (ReportLastHoursApi reportLastHoursApi : reportLastHoursApis) {
            Long createTime = reportLastHoursApi.getCreateTime();
            reportLastHoursApi.setDisplayTime(DateUtil.getHourSeconds(createTime));
        }
        return reportLastHoursApis;

    }

    @Override
    public List<ReportLastWeekRate> findModuleFailListWeekRate(ReportLastWeekRate reportLastWeekRate){

        List<ReportLastWeekRate> reportLastWeekRates=reportLastApiDao.findModuleFailListWeekRate(reportLastWeekRate);

        return reportLastWeekRates;
    }
    @Override
    public List<ReportLastWeekRate> findApiFailListWeekRate(ReportLastWeekRate reportLastWeekRate){
        List<ReportLastWeekRate> reportLastWeekRates=reportLastApiDao.findApiFailListWeekRate(reportLastWeekRate);

        return reportLastWeekRates;
    }

    public static void main(String[] args) {
        String apiUrl = "/group/user/findUserInvites";
        Matcher matcher = Pattern.compile("^(/[^/]+)/").matcher(apiUrl);
        String moduleUrl = null;
        if (matcher.find()) {
            moduleUrl = matcher.group(1);
            moduleUrl = matcher.group(1);
            moduleUrl = matcher.group(1);
        }
        System.out.print(moduleUrl);
    }

}
