package com.tunnel.deal.tcp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: Object转List工具类
 *
 * @author zs
 * @date 2023/4/13
 */
public class Obj2ListUtil {
    public static <T> List<T> objToList(Object obj, Class<T> cla){
        List<T> list = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(cla.cast(o));
            }
            return list;
        }
        return null;
    }
}
