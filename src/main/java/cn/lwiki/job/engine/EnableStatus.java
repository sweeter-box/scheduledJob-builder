package cn.lwiki.job.engine;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author sweeter
 * @date 2020/2/29
 */
public enum EnableStatus {
    /**
     * 任务启用状态
     */
    DISABLE(0, "停用"),
    ENABLE(1, "启用");
    private int value;
    @JsonValue
    private String name;

    EnableStatus(int value, String name) {
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
