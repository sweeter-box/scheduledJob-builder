package cn.lwiki.support.job.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.support.CronSequenceGenerator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author sweeter
 * @date 2020/02/29
 * 设置基类，规范定时任务的业务实现类
 */
@Slf4j
public abstract class BaseTask implements Runnable {

    private final ApplicationContext applicationContext;
    /**
     * 任务配置信息
     */
    private ScheduleJobEntity scheduleJobEntity;

    private ScheduledFuture<?> scheduledFuture;

    /**
     * 子类有且仅有该构造方法
     *
     * @param applicationContext
     */
    public BaseTask(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public ScheduleJobEntity getScheduleJobEntity() {
        return scheduleJobEntity;
    }

    public void setScheduleJobEntity(ScheduleJobEntity scheduleJobEntity) {
        this.scheduleJobEntity = scheduleJobEntity;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public void run() {
        try {
            this.execute();
            log.info("执行一次[{}]任务,完成...",scheduleJobEntity.getTitle());
            scheduleJobEntity.setLastBusinessStatus(BusinessStatus.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //表示业务执行失败
            scheduleJobEntity.setLastBusinessStatus(BusinessStatus.FAIL);
            log.error("定时任务执行完成，但业务执行失败:{}",e.getMessage());
            throw new TaskException("定时任务执行完成，但业务执行失败");
        }finally {
            this.saveExeStatus();
        }
    }

    /**
     * 执行定时任务的业务逻辑
     */
    public abstract void execute();

    /**
     * 保存执行状态
     */
    private void saveExeStatus() {
        ScheduleJobService scheduleJobService = (ScheduleJobService)applicationContext.getBean("scheduleJobService");
        scheduleJobEntity.setLastExeTime(LocalDateTime.now());
        scheduleJobEntity.setNextExeTime(getNextExeTime());
        scheduleJobService.save(scheduleJobEntity);
    }

    /**
     * 获取下一次执行时间
     * @return
     */
    private LocalDateTime getNextExeTime() {
        CronSequenceGenerator generator = new CronSequenceGenerator(scheduleJobEntity.getCron());
       return LocalDateTime.ofInstant(generator.next(new Date(System.currentTimeMillis())).toInstant(), ZoneId.systemDefault());
    }
}
