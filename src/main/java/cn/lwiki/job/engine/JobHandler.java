package cn.lwiki.job.engine;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
/**
 * @author sweeter
 * @date 2020/02/29
 */
@Lazy(false)
@Component
@Slf4j
public class JobHandler {

    private final ConcurrentHashMap<Long, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(32);

    private final ApplicationContext applicationContext;

    private final ScheduledJobTask threadPoolTaskScheduler;

    private final ScheduleJobRepository scheduleJobRepository;

    public JobHandler(ApplicationContext applicationContext, ScheduleJobRepository scheduleJobRepository, ScheduledJobTask threadPoolTaskScheduler) {
        this.applicationContext = applicationContext;
        this.scheduleJobRepository = scheduleJobRepository;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        init();
    }

    public Map<Long, ScheduledFuture> getJobsMap() {
        return jobsMap;
    }

    public void start(Long jobId) {
        if (jobsMap.containsKey(jobId)) {
            log.info("定时任务:[{}]已处于运行状态...", jobId);
            return;
        }
        executeJob(getJobDetail(jobId));
    }

    public void stop(Long jobId) {
        ScheduledFuture scheduledFuture = jobsMap.get(jobId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            jobsMap.remove(jobId);
            log.info("定时任务:[jobId={}]停止...", jobId);
        } else{
            log.info("定时任务:[jobId={}]已处于停止状态", jobId);
        }
    }

    public ScheduleJobEntity getJobDetail(Long jobId) {
        ScheduleJobEntity entity = new ScheduleJobEntity();
        entity.setId(jobId);
        entity = scheduleJobRepository.findOne(Example.of(entity)).orElseThrow(() -> new TaskException("该["+jobId+"]任务不存在"));
        entity.setRunStatus(this.getJobsMap().containsKey(entity.getId()) ? RunStatus.RUNNING:RunStatus.INACTIVE);
        return entity;
    }


    /**
     * 根据jobId重启任务
     * @param jobId
     */
    public void reset(Long jobId) {
        ScheduledFuture scheduledFuture = jobsMap.get(jobId);
        ScheduleJobEntity scheduleJob = getJobDetail(jobId);
        if (Objects.nonNull(scheduledFuture)) {
            //中断任务
            scheduledFuture.cancel(true);
            log.info("定时任务:[jobId={}]中断完成...", jobId);
        }else {
            log.info("定时任务[jobId={}]已处于stop状态，无需中断",jobId);
        }
        if (Objects.nonNull(scheduleJob)) {
            executeJob(scheduleJob);
            log.info("定时任务[jobId={}]重启完成...", scheduleJob);
        }
    }

    /**
     * 启动初始化启动所有job
     */
    private void init() {
        // 从数据库获取任务信息
        List<ScheduleJobEntity> jobs = scheduleJobRepository.findListByStatus(EnableStatus.ENABLE);
        if (!CollectionUtils.isEmpty(jobs)) {
            jobs.forEach(task ->{
                try {
                    this.executeJob(task);
                } catch (Exception e) {
                    log.error("定时任务[{}]调度失败...",task);
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 创建调度定时任务
     * @param jobEntity
     */
    private void executeJob(ScheduleJobEntity jobEntity) {
        BaseTask taskBean = getBaseTask(Objects.requireNonNull(jobEntity.getClassName()));
        taskBean.setScheduleJobEntity(jobEntity);
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(taskBean, jobEntity.getCron());
        taskBean.setScheduledFuture(schedule);
        jobsMap.put(jobEntity.getId(), schedule);
        log.info("任务:[{}] 正在运行中...", jobEntity);
    }

    /**
     * 获取BaseTask实例
     * @param className
     * @return
     */
    private BaseTask getBaseTask(String className){
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            if (ArrayUtils.isNotEmpty(declaredConstructors)){
                for (Constructor constructor: declaredConstructors) {
                    Parameter[] parameters = constructor.getParameters();
                    for (Parameter parameter: parameters) {
                        if (parameter.getType() == ApplicationContext.class) {
                           return  (BaseTask) constructor.newInstance(applicationContext);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskException("实例化".concat(className).concat("失败"));
        }
        throw new TaskException("实例化".concat(className).concat("失败"));
    }
}
