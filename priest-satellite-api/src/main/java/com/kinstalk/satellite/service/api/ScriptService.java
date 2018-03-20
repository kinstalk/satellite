package com.kinstalk.satellite.service.api;


import com.kinstalk.satellite.domain.RunScriptTimer;
import com.kinstalk.satellite.domain.Script;

import java.util.List;


/**
 * Created by zhangchuanqi on 16/4/14.
 */
public interface ScriptService {

    public List<Script> queryScriptPage();

    public Script queryScript(Long id);

    public long deleteScript(Long id);

    public long saveScript(Script script);

    public List<RunScriptTimer> findRuntierScript(Long id );


}
