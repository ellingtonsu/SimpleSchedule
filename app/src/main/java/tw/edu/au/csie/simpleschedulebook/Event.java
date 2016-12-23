package tw.edu.au.csie.simpleschedulebook;

/**
 * Created by Wei-Tsung on 2016/10/23.
 */

public class Event {

    final static int TYPE_PRIVATE = 0;
    final static int TYPE_PUBLIC = 1;

    int mId;
    String mEvent;
    String mDatetime;
    int mType; // 0: private, 1:public

    Event(String event, String datetime, int type) {
        mId = -1;
        mEvent = event;
        mDatetime = datetime;
        mType = type;
    }

    Event(int id, String event, String datetime, int type){
        mId = id;
        mEvent = event;
        mDatetime = datetime;
        mType = type;
    }

    void setId(int id) {
        mId = id;
    }

    void setEvent(String event){
        mEvent = event;
    }

    void setDatetime(String datetime){
        mDatetime = datetime;
    }

    void setType(int type) { mType = type; }

    int getId() {
        return mId;
    }

    String getEvent(){
        return mEvent;
    }

    String getDatetime(){
        return mDatetime;
    }

    int getType() { return mType; }
}