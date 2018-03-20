package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.Module;
import com.kinstalk.satellite.domain.ReportLastApi;
import com.kinstalk.satellite.domain.ReportLastWeekRate;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午2:39
 */
public interface ReportLastApiDao {

    String SELECT_ALL_FIELD = " a.`api_id` as apiId,a.`module_id` as moduleId,a.`status` ,b.lastHoursRate,b.costTime,a.`total_count` as totalCount,a.`ok_count` as okCount,a.`create_time` as createTime";

    String  SELECT_BY_MODULE = "tl.module_id as moduleId,tl.create_time,tl.agent_id as agentId,(tl.create_time+tl.sleep_time) as nextRuntime,max(costTime) as costTime, min(tl.`status`)" +
            " as `status`,TRUNCATE(TRUNCATE(sum(tl.`lastHoursRate`),2)/count(tl.module_id),2) as lastHoursRate,tl.app_id as appId";

    String INSERT_ALL_FIELD = "api_id,module_id,`status`,last_hours_rate,cost_time,total_count,ok_count,create_time";

    String INSERT_MODULE_FIELD="module_id,create_time,agent_id,next_runtime,cost_time,status,last_hours_rate,app_id,update_key";

    String SELECT_MODULE_REPORT=" tr.module_id as moduleId,tm.module_url as moduleUrl,tr.app_id as appId,tr.create_time as createTime,tr.next_runtime as nextRuntime,tm.module_name as moduleName,tr.cost_time as costTime,tr.status as status,tr.last_hours_rate as lastHoursRate,tr.agent_area as agentArea ";

    String SELECT_MODULE_WEEK_REPORT= " module_id as moduleId,count(status) as failStatus,create_time as createTime ";

    String INSERT_MODULE_WEEK_REPORT=" api_id,module_id,success_rate,status,cost_time,total_count,ok_count,create_time,agent_id,app_id ";

    String TABLE_LAST = " `report_last_api` ";

    String TABLE_MODULE=" `module` ";

    String TABLE_AGENT=" `agent` ";

    String REPORT_LAST_MODULE=" `report_last_module` ";

    String REPORT_LAST_WEEK_API="`report_last_week_api`";


    String INSERT_SUB_FIELD = "api_id,module_id,cost_time,total_count,ok_count,create_time,agent_id,app_id,timer_id";

    String JOIN_REPORT_LAST_TBALE="(  select a.module_id,a.`status`,a.`agent_id`,a.`sleep_time`,b.lastHoursRate,b.costTime,a.`create_time`,a.app_id  from report_last_api a  right join (select `api_id`,TRUNCATE(TRUNCATE(sum(`last_hours_rate`),2)/count(timer_id),2) as lastHoursRate,max(`cost_time`) as costTime,max(create_time) as `time`  from report_last_api  group by api_id ) b on a.api_id=b.api_id and a.`create_time`=b.time ) tl";

    /**
     * 新增一条结果信息
     *
     * @param reportLastApi 报表信息
     * @return
     */
    @Insert("insert into " + TABLE_LAST + " (" + INSERT_ALL_FIELD + ") " +
            "values (#{apiId},#{moduleId},#{status},#{lastHoursRate},#{costTime},#{totalCount},#{okCount},#{createTime})")
    int insert(ReportLastApi reportLastApi);

    /**
     * 查询所有接口当前状态信息
     *
     * @param moduleId
     * @return
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_LAST +"a right join (select `api_id`,TRUNCATE(TRUNCATE(sum(`last_hours_rate`),2)/count(timer_id),2) as lastHoursRate,max(`cost_time`) as costTime,max(create_time) as `time`  from "
            +TABLE_LAST+ " where app_id=#{appId} group by api_id ) b on a.api_id=b.api_id and a.`create_time`=b.time  " +
            "where a.module_id=#{moduleId} ")
    @Options(useCache = true)
    List<ReportLastApi> findAll(@Param("moduleId") Long moduleId,@Param("appId") Integer appId);

    /**
     * 查询module的出错状态
     *
     * @param reportLastWeekRate
     * @return
     */

    @Select("select " + SELECT_MODULE_WEEK_REPORT + "  from " + REPORT_LAST_WEEK_API + " where status=0 and module_id=#{moduleId} and app_id=#{appId} and create_time>#{createTime} group by create_time")
    @Options(useCache = true)
    List<ReportLastWeekRate> findModuleFailListWeekRate(ReportLastWeekRate reportLastWeekRate);

    /**
     * 查询api的出错状态
     *
     * @param reportLastWeekRate
     * @return
     */

    @Select("select " + SELECT_MODULE_WEEK_REPORT + "  from " + REPORT_LAST_WEEK_API + " where  module_id=#{moduleId} and api_id=#{apiId} and app_id=#{appId}  and create_time>=#{startTime} and create_time<=#{endTime} group by create_time")
    @Options(useCache = true)
    List<ReportLastWeekRate> findApiFailListWeekRate(ReportLastWeekRate reportLastWeekRate);
    /**
     * 查询所有接口当前状态信息
     * @return
     */
    @Select("select " + SELECT_ALL_FIELD + " from " + TABLE_LAST +"a right join (select `api_id`,TRUNCATE(TRUNCATE(sum(`last_hours_rate`),2)/count(timer_id),2) as lastHoursRate,max(`cost_time`) as costTime,max(create_time) as `time`  from "
            +TABLE_LAST+ " where app_id=#{appId} group by api_id ) b on a.api_id=b.api_id and a.`create_time`=b.time  " +
            "where status=0 ")
    @Options(useCache = true)
    List<ReportLastApi> findFailList(@Param("appId") Integer appId);



    /**
     * 批量插入记录
     *
     * @param reportLastApis 批量插入的列表
     * @return 影响的行数
     */
    @Options(useCache = true)
    @Insert("<script>" +
            "insert into " + TABLE_LAST + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"reportLastApis\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.apiId},#{item.moduleId},#{item.status},#{item.lastHoursRate},#{item.costTime},#{item.totalCount},#{item.okCount},#{item.createTime}\n" +
            "\t</foreach> </script>")
    int batchInsertLogApi(@Param("reportLastApis") List<ReportLastApi> reportLastApis);

    /**
     * 批量删除创建时间小于某时间的记录
     *
     * @param time 时间
     * @return 影响的行数
     */
    @Delete(" delete from " + TABLE_LAST + " where (create_time < #{time} and agent_id=#{agentId} and app_id=#{appId} and timer_id=#{timerId}) or (timer_id not in (select timer_id from `script_timer`))")
    int batchDeleteLogApi(@Param("time") Long time,@Param("agentId") Integer agentId,@Param("appId") Integer appId,@Param("timerId") Long timerId);

    /**
     * 批量从log_api查出再批量入最新报表
     *
     * @return 影响的行数
     */
    @Insert("<script>" +
            "insert into " + TABLE_LAST + " (" + INSERT_SUB_FIELD + ") " +
            "select api_id,module_id,AVG(cost_time),count(*),sum(`status`),MAX(create_time),#{agentId},app_id,#{timerId} from log_api where create_time=#{time} group by api_id</script>")
    @Options(useCache = true)
    int batchJoinInsert(@Param("time") Long time, @Param("agentId") Integer agentId,@Param("timerId") Long timerId);

    /**
     * 批量更新最新表里的24小时成功率
     *
     * @param time 时间点
     * @return 影响行数
     */
    @Update("<script> update " + TABLE_LAST + " c " +
            "right join (select api_id ,a.timer_id,a.succ_num/(a.succ_num+a.fail_num) as succ_rate from " +
            "(SELECT api_id,timer_id,sum(IF(`status`=1, 1, 0)) AS succ_num, sum(IF(`status`=0, 1, 0)) AS fail_num from log_api where create_time >= #{time} and timer_id=#{timerId} group by api_id) a" +
            ") b " +
            "on c.api_id=b.api_id and c.timer_id=b.timer_id " +
            "set c.last_hours_rate=b.succ_rate, " +
            "c.`status`=IF(c.ok_count/c.total_count=1, 1, 0)"+
            "where c.api_id=b.api_id;</script> ")
    @Options(useCache = true)
    int batchUpdateJoin(@Param("time") Long time,@Param("timerId") Long timerId);

    /**
     * 批量插入report_last_week_api
     *
     * @return 影响的行数
     */
    @Insert("<script>" +
            "insert into " + REPORT_LAST_WEEK_API + " (" + INSERT_MODULE_WEEK_REPORT + ") " +
            "select api_id,module_id,TRUNCATE(ok_count/total_count,2),status,cost_time,total_count,ok_count,create_time,agent_id,app_id from report_last_api where status=0 and app_id=#{appId} </script>")
    @Options(useCache = true)
    int batchInsertWeekApi(@Param("appId") Integer appId);

    /**
     * 按模块返回接口状态
     *
     * @return 按模块返回
     */
    @Insert("<script>" +
            "insert into " + REPORT_LAST_MODULE + " (" + INSERT_MODULE_FIELD + ") " +
            "select " + SELECT_BY_MODULE +",2  from " + JOIN_REPORT_LAST_TBALE + " where tl.agent_id=#{agentId}  group by tl.module_id,tl.agent_id,tl.app_id </script>")
    @Options(useCache = true)
    int batchInserReportModule(Integer agentId);


    /**
     *
     * 更新原有数据标志
     * @return
     */
    @Update("update  `report_last_module`  set update_key=3-update_key where agent_id=#{agentId}")
    int updateReportModule(@Param("agentId") Integer agentId);

    /**
     * 删除report_module表原有数据
     * @return
     */
    @Update("delete from" +REPORT_LAST_MODULE+" where update_key=2 and agent_id=#{agentId}")
    int deleteReportModule(@Param("agentId") Integer agentId);



    @Select("select"+SELECT_MODULE_REPORT+ "from"+REPORT_LAST_MODULE+" as tr, "+TABLE_MODULE+" as tm where tr.module_id=tm.module_id and update_key=1 and tr.app_id=#{appId} ")
    @Options(useCache = true)
    List<Module> batchFindModule(@Param("appId") Integer appId);


}
