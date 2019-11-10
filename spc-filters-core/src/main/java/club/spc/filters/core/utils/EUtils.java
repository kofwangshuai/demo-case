package club.spc.filters.core.utils;

import club.spc.filters.core.exception.SpcException;

import java.util.Map;

/**
 * @ClassName:EUtils
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-09 23:48
 * @Version 1.0.0
 **/
public class EUtils {

    protected Object handleException(Map<String, Object> info,
                                                 Exception ex) throws SpcException {
        int statusCode = 401;
//        Throwable cause = ex;
//        String message = ex.getFailureType().toString();
//
//        ClientException clientException = findClientException(ex);
//        if (clientException == null) {
//            clientException = findClientException(ex.getFallbackException());
//        }
//
//        if (clientException != null) {
//            if (clientException
//                    .getErrorType() == ClientException.ErrorType.SERVER_THROTTLED) {
//                statusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
//            }
//            cause = clientException;
//            message = clientException.getErrorType().toString();
//        }
        info.put("status", String.valueOf(statusCode));
        throw new SpcException(ex, "Forwarding error", statusCode, "");
    }


}
