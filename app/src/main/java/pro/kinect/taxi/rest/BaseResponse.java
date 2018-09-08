package pro.kinect.taxi.rest;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BaseResponse {

    @IntDef({UNDEFINED, SUCCESS, FAILURE, LOCAL_RESPONSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {}

    public static final int UNDEFINED = -1;

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;
    public static final int LOCAL_RESPONSE = 2;

    @Status
    private int status = UNDEFINED; // default value

    /**
     * Default constructor
     * @param status int
     */
    public BaseResponse(@Status int status) {
        this.status = status;
    }

    @Status
    public int getStatus() {
        return status;
    }

    public void setStatus(@Status int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return SUCCESS == getStatus();
    }

    public static boolean isSuccess(@Status int responseStatus) {
        return SUCCESS == responseStatus;
    }
}
