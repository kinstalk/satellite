package com.kinstalk.satellite.web;

import com.kinstalk.satellite.common.utils.SatelliteConstants;
import com.kinstalk.satellite.domain.PhonePool;
import com.kinstalk.satellite.domain.PoolConfig;
import com.kinstalk.satellite.service.api.PoolConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zhangchuanqi on 16/7/8.
 */

@Controller
@RequestMapping(value = "/pool", method = {RequestMethod.GET, RequestMethod.POST})
public class PoolConfigController {


    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);


    @Resource
    private PoolConfigService poolConfigService;

    /**
     * single Config
     * @param id
     */
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public void selectOneConfig(Long id,Model view) {

                PoolConfig poolConfig = poolConfigService.selectRemain();

                String.format(SatelliteConstants.WEB_IFRAME_SCRIPT, "查询失败！");

                view.addAttribute("PoolConfig", poolConfig);



    }

    /**
     * @param poolConfig
     * @param view
     * @return
     */

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public String save(PoolConfig poolConfig, Model view) {

        try {

            String lastMaxNumber=poolConfigService.selectMax();

            if(lastMaxNumber!=null){

                poolConfig.setLastMaxNumber(lastMaxNumber);
            }else {
                poolConfig.setLastMaxNumber("12200000000");
            }

               // long rows = poolConfigService.update(poolConfig);

                //先删除没有被占用的手机号
                long rowsDelete=poolConfigService.deletePool();
                PhonePool phonePool=new PhonePool();


                Long minNumber=Long.parseLong(poolConfig.getLastMaxNumber())+1;
                //重新给phonepool写入新配置手机号
                for(int i=1;i<=poolConfig.getGroups();i++){
                    phonePool.setPhoneStart(minNumber.toString());
                    phonePool.setPhoneEnd(String.valueOf(minNumber + (poolConfig.getSize() - 1)));
                    poolConfigService.savePool(phonePool);

                    minNumber=Long.parseLong(phonePool.getPhoneEnd()) + 1;

                }




        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "fail";
        }
        return "success";

    }

    /**
     * @param id
     * @param view
     * @return
     */
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    @ResponseBody
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model view) {
        try {
            long rows=poolConfigService.delete(id);

            return "success";


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "fail";
        }



    }
}
