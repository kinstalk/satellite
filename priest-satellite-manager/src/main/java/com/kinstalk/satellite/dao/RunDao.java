package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.RunScriptTimer;
import com.kinstalk.satellite.domain.RunScriptTimerDetail;
import com.kinstalk.satellite.domain.Script;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface RunDao {


    String SELECT_ALL_FIELD_UNION = " timer_id as id,tt.script_id as scriptId,script_name as scriptName,timer_name as name,notice_phone as noticePhone,notice_email as noticeEmail,duty as duty,run_type as runType,timer_type as timerType,first_time as firstTime,sleep_time as sleepTime,run_status as runStatus,tt.create_time as createTime ";

    String SELECT_ALL_FIELD = " timer_id as id,script_id as scriptId,timer_name as name,timer_type as timerType,first_time as firstTime,sleep_time as sleepTime,timer_status as timerStatus,run_status as runStatus,app_id as appId,notice_email as noticeEmail,notice_phone as noticePhone,create_time as createTime,run_type  as runType,duty as duty ";


    String INSERT_ALL_FIELD = " script_id,timer_name,notice_phone,notice_email,app_id,duty,run_type,timer_type,first_time,sleep_time,timer_status,create_time ";

    String TABLE_TIMER = " `script_timer` ";

    String TABLE_SCRIPT=" `script` ";


    String DETAIL_ALL_FIELD=" detail_id as id,timer_id as timerId,start_time as startTime,end_time as endTime,result_file_path as resultFilePath,success_rate as successRate,create_time as createTime ";

    String DETAIL_INSERT_ALL_FIELD=" timer_id,start_time,end_time,result_file_path,success_rate,create_time,agent_id ";

    String TABLE_DETAIL = " `script_timer_detail` ";



    @Options(useGeneratedKeys = true, keyProperty = "id", useCache = true)
    @Insert("insert into " + TABLE_TIMER + " (" + INSERT_ALL_FIELD + ") " +
            "values (#{scriptId},#{name},#{noticePhone},#{noticeEmail},#{appId},#{duty},#{runType},#{timerType},#{firstTime},#{sleepTime},#{timerStatus},#{createTime})")
    public long save(RunScriptTimer runScriptTimer);


    @Insert("insert into " + TABLE_DETAIL + " (" + DETAIL_INSERT_ALL_FIELD + ") " +
            "values (#{timerId},#{startTime},#{endTime},#{resultFilePath},#{successRate},#{createTime},#{agentId})")
    public long saveDetail(RunScriptTimerDetail runScriptTimerDetail);


    @Update("delete from" +TABLE_TIMER+"where timer_id=#{id}")
    public long delete(Long id);


    @Update("update" + TABLE_TIMER +
            "set run_status=#{runStatus},timer_status=#{timerStatus} where timer_id=#{id}")
    public long update(RunScriptTimer runScriptTimer);

    @Update("update" + TABLE_TIMER +
            "set script_id=#{scriptId},timer_name=#{name},timer_type=#{timerType},duty=#{duty},notice_phone=#{noticePhone},notice_email=#{noticeEmail},run_type=#{runType},sleep_time=#{sleepTime}   where timer_id=#{id}")
    public long updateTimer(RunScriptTimer runScriptTimer);

    @Update("update" + TABLE_TIMER +
            "set timer_status=#{timerStatus} where timer_id=#{id}")
    public long updateStatus(RunScriptTimer runScriptTimer);


    @Select("select"+ SELECT_ALL_FIELD_UNION +"from"+TABLE_TIMER+"tt,"+TABLE_SCRIPT+"ts where tt.script_id=ts.script_id order by timer_id desc")
    public List<RunScriptTimer> selectList();

    @Select("select"+ DETAIL_ALL_FIELD +"from"+TABLE_DETAIL+"where timer_id =#{id}")
    public List<RunScriptTimerDetail> selectDetailList(Long id);

    @Select("select"+ DETAIL_ALL_FIELD +"from"+TABLE_DETAIL+" where detail_id =#{id}")
    public RunScriptTimerDetail selectDetail(Long id);

    @Select("select"+ DETAIL_ALL_FIELD +"from"+TABLE_DETAIL+" where timer_id =#{timerId} and agent_id=#{agentId} and create_time>#{createTime} order by create_time desc")
    public List<RunScriptTimerDetail> selectAgentDetail(RunScriptTimerDetail runScriptTimerDetail);

    @Select("select count(timer_id) from"+TABLE_TIMER)
    public Integer selectListCount(Map<String, Object> params);

    @Select("select " + SELECT_ALL_FIELD+"from"+TABLE_TIMER+"where timer_id=#{id}")
    public RunScriptTimer selectById(Long id);

    @Select("select " + SELECT_ALL_FIELD+"from"+TABLE_TIMER+"where script_id=#{id}")
    public List<RunScriptTimer> selectByScriptId(Long id);

}
