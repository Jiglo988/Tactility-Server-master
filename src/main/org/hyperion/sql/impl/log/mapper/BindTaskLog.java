package org.hyperion.sql.impl.log.mapper;

import org.hyperion.sql.impl.log.type.TaskLog;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Created by Gilles on 2/03/2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation(BindTaskLog.LogBinderFactory.class)
public @interface BindTaskLog {

    class LogBinderFactory implements BinderFactory {

        @Override
        public Binder<BindTaskLog, TaskLog> build(final Annotation annotation) {
            return (s, b, l) -> {
                s.bind("timestamp", l.getTimestamp().toString());
                s.bind("taskname", l.getTaskName());
                s.bind("classname", l.getClassName());
                s.bind("executetime", l.getExecuteTime());
            };
        }
    }
}
