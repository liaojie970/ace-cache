package com.ace.cache;

import com.ace.cache.config.properties.RedisProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ace
 * @create 2017/11/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RedisProperties.class,AutoConfiguration.class})
@Documented
@Inherited
public @interface EnableAceCache {
}
