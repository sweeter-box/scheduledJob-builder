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

    List<ScheduleJobEntity> findAllList();
}
