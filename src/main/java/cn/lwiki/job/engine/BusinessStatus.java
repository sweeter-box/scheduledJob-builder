package cn.lwiki.job.engine;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sweeter
 * @date 2020/2/29
 */
public enum BusinessStatus {
    /**
     * 任务启用状态
     */
    FAIL(0, "失败"),
    SUCCESS(1, "成功");

    private int value;
    @JsonValue
    private String name;

    BusinessStatus(int value, String name) {
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
