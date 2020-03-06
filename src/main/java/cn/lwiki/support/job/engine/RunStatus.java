package cn.lwiki.support.job.engine;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author sweeter
 * @date 2020/2/29
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RunStatus {
    /**
     * 任务启用状态
     */
    INACTIVE(0, "停止"),
    RUNNING(1, "运行中");
    private int value;

    private String name;

    RunStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }


}
