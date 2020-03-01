package cn.lwiki.job.engine;


import java.util.List;

/**
 * @author sweeter
 * @date 2020/02/29
 */
public interface ScheduleJobService {
    /**
     * 停止定时任务
     * @param jobId
     */
    void stop(Long jobId);

    /**
     * 更新
     * @param jobId
     * @param cron
     */
    void update(Long jobId, String cron);

    /**
     *开始定时任务
     * @param jobId
     */
    void start(Long jobId);

    /**
     * 重启所有定时任务
     */
    String resetAll();

    /**
     * 重启定时任务
     * @param jobId
     */
    void reset(Long jobId);

    String stopAll();

    String startAll();

    /**
     * 保存定时任务信息
     * @param scheduleJobEntity
     */
    void save(ScheduleJobEntity scheduleJobEntity);

    /**
     * 查询所有定时任务
     * @return
     */
    List<ScheduleJobEntity> findAllList(ScheduleJobEntity entity);

    /**
     * 详情查询
     * @param jobId
     * @return
     */
    ScheduleJobEntity getJobDetail(Long jobId);

    /**
     * 查询所有启用状态的任务
     * @return
     */
    List<ScheduleJobEntity> findAllEnableList();
}
