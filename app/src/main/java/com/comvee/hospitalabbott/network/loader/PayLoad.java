package com.comvee.hospitalabbott.network.loader;


import com.comvee.hospitalabbott.network.BaseResponse;
import com.comvee.hospitalabbott.network.config.HttpFaultException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/*
* 剥离 最终数据
*/
public class PayLoad<T> implements Function<BaseResponse<T>, T> {

    @Override
    public T apply(@NonNull BaseResponse<T> tBaseResponse) throws Exception {//获取数据失败时，包装一个Fault 抛给上层处理错误
//        if(!tBaseResponse.isSuccess()){
        if (!tBaseResponse.getCode().equals("0")) {
            throw new HttpFaultException(Integer.valueOf(tBaseResponse.getCode()), tBaseResponse.getMsg());
        }

        if (tBaseResponse.getObj() == null) {
            //返回表示此 Class 所表示的实体类的 直接父类 的 Type。注意，是直接父类
            //这里type结果是 com.dfsj.generic.GetInstanceUtil<com.dfsj.generic.User>
            Type type = getClass().getGenericSuperclass();

            // 判断 是否泛型
            if (type instanceof ParameterizedType) {
                // 返回表示此类型实际类型参数的Type对象的数组.
                // 当有多个泛型类时，数组的长度就不是1了
                Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
                return (T) ptype[0];  //将第一个泛型T对应的类返回（这里只有一个）
            } else {
                return (T) "成功!";//若没有给定泛型，则返回Object类
            }
        }

        return tBaseResponse.getObj();
    }

}
