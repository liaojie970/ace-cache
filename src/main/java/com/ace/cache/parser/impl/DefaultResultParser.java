package com.ace.cache.parser.impl;

import com.ace.cache.parser.ICacheResultParser;
import com.ace.cache.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 默认缓存结果解析类
 *
 * @author wanghaobin
 * @description
 * @date 2017年5月18日
 * @since 1.7
 */
public class DefaultResultParser implements ICacheResultParser {

    @Override
    public Object parse(String value, Type type, Class<?>... origins) {
        Object result = null;
        if (StringUtils.isNoneEmpty(value)) {
            try {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type rawType = parameterizedType.getRawType();
                    if (((Class) rawType).isAssignableFrom(List.class)) {
                        result = JacksonUtils.json2list(value, (Class) parameterizedType.getActualTypeArguments()[0]);
                    }
                } else if (origins == null) {
                    result = JacksonUtils.json2pojo(value, (Class) type);
                } else {
                    result = JacksonUtils.json2pojo(value, origins[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
