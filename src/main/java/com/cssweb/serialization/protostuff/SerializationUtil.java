package com.cssweb.serialization.protostuff;


import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SerializationUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            //schema = RuntimeSchema.getSchema(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * 序列化（对象 -> 字节数组）
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        if (obj == null)
            throw new RuntimeException("obj is null");

        Class<T> cls = (Class<T>) obj.getClass();

        //这里需要处理
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);

        try {
            Schema<T> schema = getSchema(cls);

            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        if (data == null || data.length == 0)
            throw new RuntimeException("data is null");

        try {
            T instance = (T) objenesis.newInstance(cls);
            //http://www.cnblogs.com/wscit/p/6081958.html

            Schema<T> schema = getSchema(cls);

            ProtostuffIOUtil.mergeFrom(data, instance, schema);

            return instance;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
