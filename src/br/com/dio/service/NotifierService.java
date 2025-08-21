package br.com.dio.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventeEnum.CLEAR_SPACE;

public class NotifierService {
    private final Map<EventeEnum, List<EventeListener>> listeners = new HashMap<>(){{
        put(CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscribe (final EventeEnum eventType, EventeListener listener){
        var selectListener = listeners.get(eventType);
        selectListener.add(listener);
    }

    public void notify(final EventeEnum eventType){
        listeners.get(eventType).forEach((l -> l.update(eventType)));
    }
}
