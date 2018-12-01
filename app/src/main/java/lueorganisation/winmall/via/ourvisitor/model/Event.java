package lueorganisation.winmall.via.ourvisitor.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
    int event_id;
    String name;

    public Event(int event_id, String name) {
        this.event_id = event_id;
        this.name=name;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
