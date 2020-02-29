package cn.lwiki.job.example;

import cn.lwiki.job.engine.BaseTask;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;

/**
 * @author sweeter
 * @date 2020/2/29
 */
@Log
public class DemoTask extends BaseTask{
    /**
     * 子类有且仅有该构造方法
     *
     * @param applicationContext
     */
    public DemoTask(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    /**
     * 执行定时任务的业务逻辑
     */
    @Override
    public void execute() {
        log.info("这是一个定时任务.......");
    }
}
