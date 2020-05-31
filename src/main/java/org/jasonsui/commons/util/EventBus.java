package org.jasonsui.commons.util;

import com.google.common.collect.Maps;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import org.jasonsui.commons.AppEnum;

import java.util.Map;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();

    private final Map<AppEnum.Events, PublishSubject<Object>> mBusSubject = Maps.newConcurrentMap();

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public void register(AppEnum.Events key, Consumer<Object> onNext) {
        if (!mBusSubject.containsKey(key)) {
            mBusSubject.put(key, PublishSubject.create());
        }
        mBusSubject.get(key).subscribe(onNext);
    }

    public void post(AppEnum.Events key, Object val) {
        if (!mBusSubject.containsKey(key)) {
            mBusSubject.put(key, PublishSubject.create());
        }
        mBusSubject.get(key).onNext(val);
    }
}
