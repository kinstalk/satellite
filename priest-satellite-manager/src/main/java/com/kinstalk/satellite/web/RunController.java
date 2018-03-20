package com.kinstalk.satellite.web;


import com.kinstalk.satellite.common.utils.SatelliteConstants;
import com.kinstalk.satellite.domain.RunScriptTimer;
import com.kinstalk.satellite.domain.RunScriptTimerDetail;
import com.kinstalk.satellite.domain.Script;
import com.kinstalk.satellite.service.api.RunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@Controller
@RequestMapping(value = "/run", method = {RequestMethod.GET, RequestMethod.POST})
public class RunController {


    private static final Logger logger = LoggerFactory.getLogger(ScriptController.class);


    @Resource
    private RunService runService;

    /**
     * 查看任务列表
     * @param currentPage
     * @param view
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public void list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,Model view) {

        List<RunScriptTimer> result = runService.queryTimerList();//reportLastApiService
        List<Script> list = runService.queryScriptList();
        if (result == null || result.size() == 0) {
             String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");
        }

        //放入list对象。
        view.addAttribute("list", result);
        view.addAttribute("scriptlist",list);

    }

    /**
     * 查看任务详细
     * @param id
     */
    @RequestMapping(value = "/finddetail", method = {RequestMethod.POST})
    public String detail(Long id,Model view) {



        List<RunScriptTimerDetail> result = runService.queryDetailList(id);

        if (result == null || result.size() == 0) {
            String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

        }

        //放入list对象。
        view.addAttribute("detailList", result);

        return "/run/detail";

    }

    /**
     * 增加任务
     * @param id
     */
    @RequestMapping(value = "/new_edit", method = {RequestMethod.POST})
    public String addtask(Long id,Model view) {


        if(id!=null){
            RunScriptTimer runScriptTimer = runService.queryTimerById(id);

            if (runScriptTimer == null ) {
                return "fail";
            }

            view.addAttribute("runScriptTimer", runScriptTimer);
        }
        List<Script> list = runService.queryScriptList();
        if (list == null || list.size() == 0) {
            String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");
        }
        //放入list对象。

        view.addAttribute("scriptlist",list);


        return "/run/new_edit";

    }

    /**
     * 添加新任务
     * @param runScriptTimer
     * @param view
     * @return
     * @throws Throwable
     */

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public String save (RunScriptTimer runScriptTimer, Model view) {
        try {


            //新建任务的运行状态为停止状态

            runScriptTimer.setAppId(runService.queryScript(runScriptTimer.getScriptId()).getAppId());

            long rows = runService.saveTimer(runScriptTimer);
            if(rows<=0){
                return "false";
            }



            view.addAttribute("RunScriptTimer", runScriptTimer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }

        return  "success";

    }

    /**
     * 运行
     * @param id
     */
    @RequestMapping(value = "/run_timer", method = {RequestMethod.POST})
    @ResponseBody
    public String run (Long id) {
        try {

            RunScriptTimer runScriptTimer=runService.queryTimerById(id);

           return  runService.run(id);



        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }


        return "success";

    }

    /**
     * 停止
     * @param id
     */
    @RequestMapping(value = "/run_stop", method = {RequestMethod.POST})
    @ResponseBody
    public String stop (Long id) {
        try {

            RunScriptTimer runScriptTimer=runService.queryTimerById(id);

            if(runScriptTimer!=null&runScriptTimer.getTimerStatus()==2){
                runService.stop(id);
                return "run";
            }


            runService.stop(id);
            String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "运行成功！");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


        return "success";
    }

    /**
     * 删除任务
     * @param id
     * @param view
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    @ResponseBody
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view) {
        try {

            RunScriptTimer runScriptTimer=runService.queryTimerById(id);
            if(runScriptTimer!=null&(runScriptTimer.getTimerStatus()==0||runScriptTimer.getTimerStatus()==3)){
                long rows = runService.deleteTimer(id);
                return "success";
            }else{

                return "run";
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "fail";
        }



    }
    /**
     * 下载
     * @return
     */
    @RequestMapping(value = "/download", method = {RequestMethod.DELETE})

    public void download(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view, HttpServletResponse response) {
        try {


            if (id != null && id.longValue() > 0) {

                String path = runService.queryDetail(id).getResultFilePath();
//                String path = "/Users/keysilence/Downloads/main-release_force.apk";
                // path是指欲下载的文件的路径。
                File file = new File(path);
                if (!file.exists()) {
                    return;
                }
                // 取得文件名。
                String filename = file.getName();
                // 取得文件的后缀名。
                String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(path));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }




}
