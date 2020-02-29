package cn.lwiki.job.engine;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author sweeter
 * @date 2020/02/29
 */
@Service("scheduleJobService")
@Transactional(rollbackFor = Exception.class)
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private final ScheduleJobRepository scheduleJobRepository;

    private final JobHandler jobHandler;

    public ScheduleJobServiceImpl(ScheduleJobRepository scheduleJobRepository , JobHandler jobHandler) {
        this.scheduleJobRepository = scheduleJobRepository;
        this.jobHandler = jobHandler;
    }

    @Override
    public void update(Long jobId, String cron) {
        ScheduleJobEntity scheduleJob =  scheduleJobRepository.getOne(jobId);
        scheduleJob.setId(jobId);
        scheduleJob.setCron(cron);
        scheduleJobRepository.save(scheduleJob);
        jobHandler.reset(jobId);
    }

    @Override
    public void stop(Long jobId) {
        jobHandler.stop(jobId);
    }

    @Override
    public void start(Long jobId) {
        jobHandler.start(jobId);
    }

    @Override
    public String resetAll() {
        List<ScheduleJobEntity> scheduleJobList = scheduleJobRepository.findAllEnableList();
        if (!CollectionUtils.isEmpty(scheduleJobList)) {
            scheduleJobList.forEach(scheduleJob -> this.reset(scheduleJob.getId()));
            return "重启所有启用的定时任务完成";
        }else {
            return "无任何启用的定时任务";
        }
    }

    @Override
    public void reset(Long jobId) {
        jobHandler.reset(jobId);
    }

    @Override
    public String stopAll() {
        List<ScheduleJobEntity> scheduleJobList = scheduleJobRepository.findAllEnableList();
        if (!CollectionUtils.isEmpty(scheduleJobList)) {
            scheduleJobList.forEach(scheduleJob -> this.stop(scheduleJob.getId()));
            return "停止所有定时任务完成";
        }else {
            return "无任何定时任务可以停止";
        }
    }

    @Override
    public String startAll() {
        List<ScheduleJobEntity> scheduleJobList = scheduleJobRepository.findAllEnableList();
        if (!CollectionUtils.isEmpty(scheduleJobList)) {
            scheduleJobList.forEach(scheduleJob -> this.start(scheduleJob.getId()));
            return "启动所有定时任务完成";
        }else {
            return "无任何启用的定时任务";
        }
    }

    /**
     * 保存定时任务信息
     *
     * @param scheduleJobEntity
     */
    @Override
    public void save(ScheduleJobEntity scheduleJobEntity) {
        scheduleJobRepository.save(scheduleJobEntity);
    }

    @Override
    public List<ScheduleJobEntity> findAllList() {

        return scheduleJobRepository.findAll();
    }

}
