package club.spc.filters.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 代表一个业务错误, 对具体某个业务错误的信息描述
 *
 * 业务逻辑处理过程中, 如果需要将业务错误返回上层调用, 则需要构造ErrorMessage, 以Result的形式返回
 *
 * @author lianghaijun
 * @date 2018/9/19
 */
@Getter
@Setter
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 6575337161544520922L;

    //错误码
    private String errorCode;

    //错误描述
    private String message;

    //可对外展示的错误消息, 比如展示给用户看的错误消息
    private String displayMessage;

    public static ErrorMessage of(String errorCode, String message) {
        return new ErrorMessage(errorCode, message, null);
    }

    public static ErrorMessage of(String errorCode, String message, String displayMessage) {
        return new ErrorMessage(errorCode, message, displayMessage);
    }

    private ErrorMessage(String errorCode, String message, String displayMessage) {
        this.errorCode = errorCode;
        this.message = message;
        this.displayMessage = displayMessage;
    }
}
