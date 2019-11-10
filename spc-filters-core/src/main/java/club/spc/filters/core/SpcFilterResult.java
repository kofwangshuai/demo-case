package club.spc.filters.core;


/**
 * @ClassName:SpcFilterResult
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:49
 * @Version 1.0.0
 **/
public class SpcFilterResult {

    private Object result;
    private Throwable exception;
    private ExecutionStatusEnum status;

    public SpcFilterResult(Object result, ExecutionStatusEnum status) {
        this.result = result;
        this.status = status;
    }

    public SpcFilterResult(ExecutionStatusEnum status) {
        this.status = status;
    }

    public SpcFilterResult() {
        this.status = ExecutionStatusEnum.DISABLED;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }
    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @return the status
     */
    public ExecutionStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ExecutionStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the exception
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
