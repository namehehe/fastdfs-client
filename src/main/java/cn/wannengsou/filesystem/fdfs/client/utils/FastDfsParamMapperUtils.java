package cn.wannengsou.filesystem.fdfs.client.utils;

import cn.wannengsou.filesystem.fdfs.client.exception.FastDfsColumnMapException;
import cn.wannengsou.filesystem.fdfs.client.mapper.FieldMateData;
import cn.wannengsou.filesystem.fdfs.client.mapper.ObjectMateData;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Param对象与byte映射器 工具
 * 作者：LiZW <br/>
 * 创建时间：2016/11/20 1:32 <br/>
 */
public class FastDfsParamMapperUtils {

    private FastDfsParamMapperUtils() {
    }

    /**
     * 对象映射缓存
     */
    private static ConcurrentMap<String, ObjectMateData> mapCache = new ConcurrentHashMap<>();

    /**
     * 将byte解码为对象
     */
    public static <T> T map(byte[] content, Class<T> genericType, Charset charset) {
        // 获取映射对象
        ObjectMateData objectMap = getObjectMap(genericType);
//        if (logger.isDebugEnabled()) {
//            objectMap.dumpObjectMateData();
//        }
        try {
            return mapByIndex(content, genericType, objectMap, charset);
        } catch (InstantiationException | IllegalAccessException ie) {
//            logger.debug("Cannot instantiate: ", ie);
            throw new FastDfsColumnMapException(ie);
        }
    }

    public static <T> T map(byte[] content, Class<T> genericType) {
       return map(content, genericType, Charset.defaultCharset());
    }

    /**
     * 获取对象映射定义
     */
    public static <T> ObjectMateData getObjectMap(Class<T> genericType) {
        if (null == mapCache.get(genericType.getName())) {
            // 还未缓存过
            mapCache.put(genericType.getName(), new ObjectMateData(genericType));
        }
        return mapCache.get(genericType.getName());
    }

    /**
     * 按列顺序映射
     */
    private static <T> T mapByIndex(byte[] content, Class<T> genericType, ObjectMateData objectMap, Charset charset)
            throws InstantiationException, IllegalAccessException {
        List<FieldMateData> mappingFields = objectMap.getFieldList();
        T obj = genericType.newInstance();
        for (FieldMateData field : mappingFields) {
            // 设置属性值
//            if (logger.isTraceEnabled()) {
//                logger.trace("设置值是 " + field + field.getValue(content, charset));
//            }
            ReflectionsUtils.setFieldValue(obj, field.getFieldName(), field.getValue(content, charset));
        }
        return obj;
    }

    /**
     * 序列化为Byte
     */
    public static byte[] toByte(Object object, Charset charset) {
        ObjectMateData objectMap = getObjectMap(object.getClass());
        try {
            return convertFieldToByte(objectMap, object, charset);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ie) {
//            logger.debug("Cannot invoke get methed: ", ie);
            throw new FastDfsColumnMapException(ie);
        }
    }

    /**
     * 将属性转换为byte
     */
    private static byte[] convertFieldToByte(ObjectMateData objectMap, Object object, Charset charset)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<FieldMateData> mappingFields = objectMap.getFieldList();
        // 获取报文长度 (固定长度+动态长度)
        int size = objectMap.getFieldsSendTotalByteSize(object, charset);
        byte[] result = new byte[size];
        int offsize = 0;
        for (FieldMateData field : mappingFields) {
            byte[] fieldByte = field.toByte(object, charset);
            if (null != fieldByte) {
                System.arraycopy(fieldByte, 0, result, offsize, fieldByte.length);
                offsize += fieldByte.length;
            }
        }
        return result;
    }
}
