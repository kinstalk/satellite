package com.kinstalk.satellite.web;


import com.kinstalk.satellite.common.UploadResult;
import com.kinstalk.satellite.common.utils.UploadFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/upload", method = {RequestMethod.GET, RequestMethod.POST})
public class UploadAction {

    private static final Logger logger = LoggerFactory.getLogger(UploadAction.class);


    @RequestMapping(value = "/file", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadFile(@RequestParam(value = "Filedata", required = false) MultipartFile file,Model view) {

        try {
            UploadResult uploadResult = UploadFileUtils.uploadFile("", "script", file);
            if (uploadResult != null) {
                return uploadResult.getUrl();
            }
        } catch (Exception e) {
			e.printStackTrace();
            logger.error(e.getMessage(), e);

            return "exception";
        }
        return "success";
    }





}
