package com.kinstalk.satellite.web;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kinstalk.satellite.common.utils.DateUtil;
import com.kinstalk.satellite.domain.*;
import com.kinstalk.satellite.service.api.ReportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午6:55
 */
@Controller
@RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST})
public class ReportController {

    private static Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Resource
    private ReportService reportService;



    /**
     * 接口当前状态列表
     *
     * @return String
     */
    @RequestMapping(value = "/module_list")
    public String module_list(Model view,Integer appId) {

        view.addAttribute("appId", appId);
        return "/report/module_list";
    }
    @RequestMapping(value = "/module_data")
    @ResponseBody
    public String module_data(Integer appId) {
        List<Module> result = reportService.findModuleList(appId);

        Long endTime=System.currentTimeMillis();
        Long startTime=endTime-(7*24*3600*1000);

        ReportLastWeekRate reportLastWeekRate=new ReportLastWeekRate();
        reportLastWeekRate.setAppId(appId);
        reportLastWeekRate.setCreateTime(startTime);

        JsonArray menuJsonArray = new JsonArray();
        if(result!=null&&result.size()>0){


            for(Module module:result){
                reportLastWeekRate.setModuleId(module.getModuleId());
                JsonObject menuJson = new JsonObject();
                List<ReportLastWeekRate> listMoudleCount=reportService.findModuleFailListWeekRate(reportLastWeekRate);

                menuJson.addProperty("moduleId", module.getModuleId());
                menuJson.addProperty("moduleUrl",module.getModuleUrl());
                menuJson.addProperty("moduleName", module.getModuleName() != null ? module.getModuleName(): module.getModuleUrl());

                menuJson.addProperty("status", module.getStatus());
                menuJson.addProperty("sucRate",module.getSucRate());
                menuJson.addProperty("costTime", module.getCostTime());
                menuJson.addProperty("createTime",module.getCreateTime());
                menuJson.addProperty("appId",module.getAppId());
                menuJson.addProperty("countString",preparestat(listMoudleCount, startTime, 3600 * 1000, 168, "MM/dd HH:00"));


                menuJsonArray.add(menuJson);
            }



        }

        return new Gson().toJson(menuJsonArray).toString();
    }


    /**
     * 接口当前状态列表
     *
     * @return String
     */
    @RequestMapping(value = "/api_list", method = RequestMethod.GET)
    public String api_list(Model view, Long moduleId,Integer appId,String time) {
        List<ReportLastApi> result = reportService.findCurrentList(moduleId,appId);
        if (result == null || result.size() == 0) {
            result = null;
        }
        try{
            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Calendar a=Calendar.getInstance();
            Long startTime=0l;
            Long endTime=0l;

            if(time!=null){
                Date date=simpleDateFormat.parse(a.get(Calendar.YEAR)+"/"+time);

                startTime = date.getTime()-(3600*1000);

                if((System.currentTimeMillis()-date.getTime())<(3600*1000)){
                    endTime=System.currentTimeMillis();
                }else{
                    endTime=date.getTime()+(3600*1000);
                }

            }else {

                 endTime = System.currentTimeMillis();

                 startTime=endTime-(3600*1000*2);
            }

            ReportLastWeekRate reportLastWeekRate=new ReportLastWeekRate();
            reportLastWeekRate.setAppId(appId);
            reportLastWeekRate.setStartTime(startTime);
            reportLastWeekRate.setEndTime(endTime);
            reportLastWeekRate.setModuleId(moduleId);

            for(ReportLastApi reportLastApi:result){

                reportLastWeekRate.setApiId(reportLastApi.getApiId());

                List<ReportLastWeekRate> listApiCount=reportService.findApiFailListWeekRate(reportLastWeekRate);

                reportLastApi.setCountString(preparestat(listApiCount,startTime,300*1000,24,"MM/dd HH:00"));


            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }


        //放入list对象。
        view.addAttribute("list", result);
        view.addAttribute("appId",appId);

        return "/report/api_list";
    }

    /**
     * 失败接口状态列表
     *
     * @return
     */
    @RequestMapping(value = "/failapi_list", method = RequestMethod.GET)
    @ResponseBody
    public String failapi_list(Model view, Long moduleId,Integer appId) {
        List<ReportLastApi> result = reportService.findFailList(appId);
        StringBuffer apiurl=new StringBuffer();
        if (result != null && result.size() > 0) {
            for(ReportLastApi reportLastApi:result){

                apiurl.append(reportLastApi.getApiUrl()+"\n");

            }
        }

        //放入list对象。
        view.addAttribute("apiurl", apiurl);

        return apiurl.toString();
    }

    /**
     * 接口过去一周状态列表
     *
     * @return
     */
    @RequestMapping(value = "/week_list", method = RequestMethod.POST)
    public String week_list(Integer time, Model view,Integer appId) {
        //如果时间为空,默认拉取一周的数据
        Long checkTime;
        if (time == null || time <= 0) {
            checkTime = DateUtil.getBeforeWeekDate(6);
        } else {
            checkTime = DateUtil.getBeforeWeekDate(time);
        }
        List<String> dates = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            dates.add(DateUtil.getBeforeWeek(i));
        }
        view.addAttribute("date", dates);

        List<ReportWeekResult> result = reportService.findWeekList(checkTime,appId);
        if (result == null || result.size() == 0) {
            result = null;
        }



        //放入list对象。
        view.addAttribute("list", result);

        return "/report/week_list";
    }

    /**
     * 查看某个接口详情信息
     *
     * @return
     */
    @RequestMapping(value = "/api_detail", method = RequestMethod.POST)
    public String detail(Long apiId, Model view) {

        List<ReportLastHoursApi> result = reportService.findByApiId(apiId);
        List<String> timeList = new ArrayList<>();
        List<Long> dataList = new ArrayList<>();

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                ReportLastHoursApi reportLastHoursApi = result.get(i);
                timeList.add(i, "\"" + reportLastHoursApi.getDisplayTime() + "\"");
                dataList.add(i, reportLastHoursApi.getSucRate());
            }
        }

        //放入list对象。
        view.addAttribute("timeList", timeList);
        view.addAttribute("dataList", dataList);

        return "/report/api_detail";
    }

    /**
     * 拼装module的图表数据
     * @param listline
     * @return
     */
    public String preparestat(List<ReportLastWeekRate> listline,Long startTime,Integer increment,Integer size,String format){

        Map<String, Integer> mapline = new TreeMap<String,Integer>();

        DateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();



        for(int i=0;i<=size;i++){
            calendar.setTimeInMillis(startTime);
            mapline.put(formatter.format(calendar.getTime()),0);
            startTime=startTime+(increment);
        }


        if(listline!=null&&listline.size()>0){
            for (ReportLastWeekRate reportLastWeekRate : listline) {

                mapline.put(reportLastWeekRate.getDatetime(), reportLastWeekRate.getFailStatus());

            }

            if(!listline.get(listline.size()-1).getDatetime().equals(formatter.format(calendar.getTime())))
            {
                mapline.put(formatter.format(calendar.getTime()),0);
            }
        }

        Map<String, Object> maptemp = new HashMap<String, Object>();

        maptemp.put("line", mapline);


        return JSONObject.toJSONString(maptemp);

    }



}
