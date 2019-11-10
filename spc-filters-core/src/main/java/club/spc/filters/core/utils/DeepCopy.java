package club.spc.filters.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @ClassName:DeepCopy
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:31
 * @Version 1.0.0
 **/
@Slf4j
public class DeepCopy {

    /**
     *  实现深copy的方式 包含 1 通过构造器 ，2 通过重写clone ，3通过实现序列化的方式
     *
     * @exception NotSerializableException
     */
    public static Object copy(Object orig) throws NotSerializableException {

        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (NotSerializableException e) {
            log.error("NotSerializableException ",e);
            throw e;
        } catch (IOException e) {
            log.error("IOException ",e);
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            log.error("ClassNotFoundException ",cnfe);
            cnfe.printStackTrace();
        }
        return obj;
    }
}
