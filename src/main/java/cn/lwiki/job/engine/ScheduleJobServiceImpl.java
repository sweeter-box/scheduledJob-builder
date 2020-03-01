package cn.lwiki.job.engine;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        ScheduleJobEntity scheduleJob = this.getJobDetail(jobId);
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
        List<ScheduleJobEntity> scheduleJobList = this.findAllEnableList();
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
        List<ScheduleJobEntity> scheduleJobList = this.findAllEnableList();
        if (!CollectionUtils.isEmpty(scheduleJobList)) {
            scheduleJobList.forEach(scheduleJob -> this.stop(scheduleJob.getId()));
            return "停止所有定时任务完成";
        }else {
            return "无任何定时任务可以停止";
        }
    }

    @Override
    public String startAll() {
        List<ScheduleJobEntity> scheduleJobList = this.findAllEnableList();
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
    public List<ScheduleJobEntity> findAllList(ScheduleJobEntity entity) {
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", ExampleMatcher.GenericPropertyMatcher::contains).
                withMatcher("className", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<ScheduleJobEntity> example = Example.of(entity, matcher);
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        List<ScheduleJobEntity> allList = scheduleJobRepository.findAll(example,sort);
        return this.loadRunStatus(allList);
    }

    @Override
    public ScheduleJobEntity getJobDetail(Long jobId) {
        ScheduleJobEntity entity = new ScheduleJobEntity();
        entity.setId(jobId);
        entity = scheduleJobRepository.findOne(Example.of(entity)).orElseThrow(() -> new TaskException("该["+jobId+"]任务不存在"));
        entity.setRunStatus(jobHandler.getJobsMap().containsKey(entity.getId()) ? RunStatus.RUNNING:RunStatus.INACTIVE);
        return entity;
    }

    @Override
    public List<ScheduleJobEntity> findAllEnableList() {
        List<ScheduleJobEntity> allList = scheduleJobRepository.findListByStatus(EnableStatus.ENABLE);
        return this.loadRunStatus(allList);
    }

    /**
     * 加载任务的当前运行状态
     * @param jobList
     * @return
     */
    private List<ScheduleJobEntity> loadRunStatus(List<ScheduleJobEntity> jobList) {
        if (CollectionUtils.isEmpty(jobList)) {
            return Collections.emptyList();
        }else {
            return jobList.stream()
                    .peek(job -> job.setRunStatus(jobHandler.getJobsMap().containsKey(job.getId()) ? RunStatus.RUNNING:RunStatus.INACTIVE))
                    .collect(Collectors.toList());
        }
    }

}
