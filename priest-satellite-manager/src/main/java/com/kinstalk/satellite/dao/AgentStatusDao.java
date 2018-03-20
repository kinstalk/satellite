package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.AgentModel;
import com.kinstalk.satellite.domain.AgentStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by zhangchuanqi on 16/6/24.
 */
public interface AgentStatusDao {

    String SELECT_ALL_FIELD=" status_id as statusId,timer_id as timerId,agent_id as agentId,status as status,last_runtime as lastRuntime,update_time as updateTime ";

    String SELECT_DETAIL_FIELD=" ta.agent_area as agentName ,status_id as statusId,ts.timer_id as timerId,tt.timer_name as timerName,ta.agent_id as agentId,status as status,ts.last_runtime as lastRuntime,ts.update_time as updateTime ";

    String INSERT_ALL_FIELD = " timer_id,status,agent_id,last_runtime,update_time ";

    String TABLE_STATUS = " `agent_status` ";

    String TABLE_TIMER=" `script_timer` ";

    String TABLE_AGENT =" `agent` ";


    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = true)
    @Insert("insert into " + TABLE_STATUS + " (" +INSERT_ALL_FIELD + ") " + "values (#{timerId},#{status},#{agentId},#{lastRuntime},#{updateTime})")
    public long save(AgentStatus agentStatus);


    @Update("update" + TABLE_STATUS +
            "set status=#{status},last_runtime=#{lastRuntime},update_time=#{updateTime} where agent_id=#{agentId} and timer_id=#{timerId}")
    public long update(AgentStatus agentStatus);

    @Update("update" + TABLE_STATUS +
            "set status=3 where  timer_id=#{timerId}")
    public long updatebyid(Long id);

    @Select("select"+ SELECT_ALL_FIELD +"from"+TABLE_STATUS+"where agent_id =#{agentId} and timer_id =#{timerId}")
    public AgentStatus selectById(AgentStatus agentStatus);

    @Select("select"+ SELECT_DETAIL_FIELD +"from"+TABLE_STATUS+" as ts,"+TABLE_TIMER+"as tt,"+TABLE_AGENT +" as ta  where ts.agent_id =ta.agent_id and ts.timer_id=tt.timer_id and ts.status<>0")
    public List<AgentStatus> selectByAgentid();
}
