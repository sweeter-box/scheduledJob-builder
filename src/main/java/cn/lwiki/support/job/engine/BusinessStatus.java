package cn.lwiki.support.job.engine;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author sweeter
 * @date 2020/2/29
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessStatus {
    /**
     * 任务启用状态
     */
    FAIL(0, "失败"),
    SUCCESS(1, "成功");

    private int value;

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
