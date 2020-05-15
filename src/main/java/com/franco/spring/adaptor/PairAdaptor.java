package com.franco.spring.adaptor;

import com.franco.spring.adaptor.injector.ArrayInjector;
import com.franco.spring.adaptor.injector.NameInjector;
import com.franco.spring.adaptor.injector.NullInjector;
import com.franco.spring.adaptor.injector.ParamInjector;
import com.franco.spring.annotation.RequestParam;

/**
 * 匹配适配器
 *
 * @author franco
 */
public class PairAdaptor extends AbstractAdaptor {

    @Override
    protected ParamInjector evalInjector(Class<?> clazz, RequestParam requestParam) {
        if(requestParam == null) {
            return new NullInjector(clazz);
        }
        if(clazz.isArray()) {
            return new ArrayInjector(requestParam.value(), clazz);
        }
        return new NameInjector(requestParam.value(), clazz);
    }
}
