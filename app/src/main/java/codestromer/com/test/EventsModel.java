package codestromer.com.test;

public class EventsModel {


    private String description;
    private String eventName;
    private String eventdate;
    private String reqiredVol;

    private EventsModel() {}

    private EventsModel(String descreption,String eventName, String eventdate,String reqiredVol){
            this.eventName = eventName;
            this.eventdate = eventdate;
            this.description = descreption;
            this.reqiredVol = reqiredVol;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getReqiredVol() {
        return reqiredVol;
    }

    public void setReqiredVol(String reqiredVol) {
        this.reqiredVol = reqiredVol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }
}
