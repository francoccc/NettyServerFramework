package com.franco.spring.annotation;

import com.franco.spring.view.ResponseView;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface View {

    String name();

    Class<? extends ResponseView> type();
}
