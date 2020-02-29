package cn.lwiki.job.engine;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sweeter
 * @date 2020/2/29
 */
public enum RunStatus {
    /**
     * 任务启用状态
     */
    INACTIVE(0, "停止"),
    RUNNING(1, "运行中");
    private int value;
    @JsonValue
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
