package club.spc.filters.core;

/**
 * @ClassName:ExecutionStatus
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:50
 * @Version 1.0.0
 *
 *  1  过滤器执行的状态 ；成功，跳过，禁用，失败。
 *  2  链路追踪和异常计数器的核心指标数据
 *  3  控制执行器的核心数据
 *
 **/
public enum ExecutionStatusEnum {

    SUCCESS (1), SKIPPED(-1), DISABLED(-2), FAILED(-3);

    private int status;

    ExecutionStatusEnum(int status) {
        this.status = status;
    }
}
