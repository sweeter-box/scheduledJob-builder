package cn.lwiki.job.engine;

/**
 * @author sweeter
 * @date 2019/12/17
 */
public class TaskException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public TaskException() {
        super();
    }

    public TaskException(String message) {
        super(message);
    }

    public TaskException(Throwable cause) {
        super(cause);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
