package com.paypal.test.annotations;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Created by rahulaw on 28/01/17.
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE,METHOD,FIELD,PARAMETER})
public @interface Secured { }