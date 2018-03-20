package com.kinstalk.satellite.web;


import com.kinstalk.satellite.domain.Script;
import com.kinstalk.satellite.service.api.ScriptService;
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
@RequestMapping(value = "/script", method = {RequestMethod.GET, RequestMethod.POST})
public class ScriptController {

    private static final Logger logger = LoggerFactory.getLogger(ScriptController.class);

    @Resource
    private ScriptService scriptService;


    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public void list(@RequestParam(value = "currentPage", required = false, defaultValue = "0") int currentPage,Model view) {

        List<Script> result = scriptService.queryScriptPage();//reportLastApiService
        //放入list对象。
        view.addAttribute("list", result);
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
	@ResponseBody
    public String save(Script script, Model view) {
        try {
            long rows = scriptService.saveScript(script);
            view.addAttribute("Script", script);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
		return "success";
    }





    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    @ResponseBody
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view) {

        try {


            if(scriptService.findRuntierScript(id)!=null&&scriptService.findRuntierScript(id).size()>0){
                return "script";
            }
            long rows = scriptService.deleteScript(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return  "false";
        }
        return "success";
    }

    /**
     * 查询一条script
     * @param id
     */
    @RequestMapping(value = "/new_edit", method = {RequestMethod.POST})
    public String findupdate(Long id,Model view) {

		if(id != null && id > 0) {
			
			Script script = scriptService.queryScript(id);
	
			if (script == null ) {
				return "fail";
			}
	
			//放入list对象。
			view.addAttribute("script", script);
		}

        return "/script/new_edit";

    }
    /**
     * 下载
     * @return
     */
    @RequestMapping(value = "/download", method = {RequestMethod.DELETE})

    public void download(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view, HttpServletResponse response) {
        try {


            if (id != null && id.longValue() > 0) {

                String path = scriptService.queryScript(id).getPath();
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
