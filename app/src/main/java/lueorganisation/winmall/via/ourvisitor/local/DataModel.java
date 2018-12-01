package lueorganisation.winmall.via.ourvisitor.local;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModel {

    String name;
    String type;
    String address;
    String time;


    public DataModel(String name, String type, String address, String time ) {
        this.name=name;
        this.type=type;
        this.address=address;
        this.time=time;

    }


    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }


    public String getAddress() {
        return address;
    }


    public String getTime() {
        return time;
    }

}
