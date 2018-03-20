package com.kinstalk.satellite.dao;

import com.kinstalk.satellite.domain.LogApi;
import com.kinstalk.satellite.domain.ReportDayApi;
import com.kinstalk.satellite.domain.ReportLastApi;
import com.kinstalk.satellite.domain.ReportLastHoursApi;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * User: liuling
 * Date: 16/4/12
 * Time: 下午2:30
 */
public interface LogApiDao {

    String SELECT_ALL_FIELD = "api_id as apiId,module_id as moduleId,`status`,cost_time as costTime,create_time as createTime";

    String INSERT_ALL_FIELD = "api_id,module_id,`status`,cost_time,agent_id,app_id,timer_id,create_time";

    String SELECT_ALL_FIELD_RESULT = SELECT_ALL_FIELD + ",a.succ_num/(a.succ_num+a.fail_num) as lastHoursRate ";

    String SELECT_ALL_FIELD_LAST_HOURS = "api_id as apiId,module_id as moduleId,a.succ_num/(a.succ_num+a.fail_num) as successRate , (a.succ_num+a.fail_num) as totalCount,maxCostTime,minCostTime,aveCostTime,agent_id as agentId,app_id as appId";

    String TABLE_LOG = " `log_api` ";

    /**
     * 新增一条结果流水信息
     *
     * @param logApi 接口信息对象
     * @return 影响的行数
     */
    @Insert(
            "insert into " + TABLE_LOG + " (" + INSERT_ALL_FIELD + ") " +
                    "values (#{apiId},#{moduleId},#{status},#{costTime},#{agentId},#{createTime})")
    int insertLogApi(LogApi logApi);

    /**
     * 根据创建时间和接口id批量查询模块的成功和失败数
     *
     * @param createTime 时间点
     * @return 模块信息列表
     */
    @Select("<script> \n" +
            "select " + SELECT_ALL_FIELD_RESULT + "\n" +
            "from " +
            "(SELECT api_id,module_id,status,cost_time,create_time, sum(IF(`status`=1, 1, 0)) AS succ_num, sum(IF(`status`=0, 1, 0)) AS fail_num FROM " + TABLE_LOG + "  WHERE" +
            "\tcreate_time >= #{createTime}\n" +
            "\tgroup by api_id) a\n" +
            "</script>")
    @Options(useCache = true)
    List<ReportLastApi> batchFindByTime(@Param("createTime") Long createTime);

    /**
     * 批量插入记录
     *
     * @param apis 批量插入的列表
     * @return 影响的行数
     */
    @Insert("<script>" +
            "insert ignore into " + TABLE_LOG + " (" + INSERT_ALL_FIELD + ") values" +
            "<foreach item=\"item\" index=\"index\" collection=\"apis\" open=\"(\" separator=\"),(\" close=\")\">\n" +
            "\t#{item.apiId},#{item.moduleId},#{item.status},#{item.costTime},#{item.agentId},#{item.appId},#{item.timerId},#{item.createTime}\n" +
            "\t</foreach> </script>")
//    @Options(useCache = true)
    int batchInsertLogApi(@Param("apis") List<LogApi> apis);

    /**
     * 根据创建时间和接口id批量查询模块的成功和失败数
     *
     * @param createTime 时间点
     * @return 模块信息列表
     */
    @Select("<script> \n" +
            "select " + SELECT_ALL_FIELD_LAST_HOURS + "\n" +
            "from " +
            "(SELECT api_id,module_id,sum(IF(`status`=1, 1, 0)) AS succ_num, sum(IF(`status`=0, 1, 0)) AS fail_num \n" +
            ",max(cost_time) as maxCostTime,\n" +
            "min(cost_time) as minCostTime,avg(cost_time) as aveCostTime,agent_id,app_id FROM " + TABLE_LOG + "  WHERE" +
            "\tcreate_time >= #{createTime}\n" +
            "\tgroup by api_id) a\n" +
            "</script>")
    @Options(useCache = true)
    List<ReportLastHoursApi> batchFindLastHoursByTime(@Param("createTime") Long createTime);

    /**
     * 查询某时间段内接口id批量查询模块的成功和失败数
     *
     * @param startTime 起始时间
     * @param endTime   截至时间
     * @return 模块信息列表
     */
    @Select("<script> \n" +
            "select " + SELECT_ALL_FIELD_LAST_HOURS + "" +
            " from " +
            " (SELECT api_id,module_id,sum(IF(`status`=1, 1, 0)) AS succ_num, sum(IF(`status`=0, 1, 0)) AS fail_num" +
            " ,max(cost_time) as maxCostTime,\n" +
            " min(cost_time) as minCostTime,avg(cost_time) as aveCostTime,agent_id,app_id FROM " + TABLE_LOG + "  WHERE" +
            " create_time between #{startTime} and #{endTime}" +
            " group by api_id,agent_id ) a" +
            "</script>")
    @Options(useCache = true)
    List<ReportDayApi> batchFindDaysByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

}
