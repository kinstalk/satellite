package com.kinstalk.satellite.service.api;/*
package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.common.page.Page;
import com.kinstalk.satellite.model.RunScriptTimer;


/**
 * Created by zhangchuanqi on 16/4/14.
*/

import com.kinstalk.satellite.domain.RunScriptTimer;
import com.kinstalk.satellite.domain.RunScriptTimerDetail;
import com.kinstalk.satellite.domain.Script;

import java.util.List;

public interface RunService {

    public List<RunScriptTimer> queryTimerList();

    public RunScriptTimer queryTimerById(Long id);

    public List<Script> queryScriptList();

    public Script queryScript(Long id);

    public long deleteTimer(Long id);

    public long saveTimer(RunScriptTimer runScriptTimer);

    public String run(Long id);

    public void stop(Long id);

    public List<RunScriptTimerDetail> queryDetailList(Long id);

    public RunScriptTimerDetail queryDetail(Long id);


}