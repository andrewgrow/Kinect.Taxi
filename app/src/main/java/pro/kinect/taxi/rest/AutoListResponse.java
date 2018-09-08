package pro.kinect.taxi.rest;

import android.support.annotation.Nullable;

import java.util.List;

import pro.kinect.taxi.db.EntityAuto;

public class AutoListResponse extends BaseResponse {

    private List<EntityAuto> autoList;

    public AutoListResponse(@BaseResponse.Status int status, @Nullable List<EntityAuto> autoList) {
        super(status);
        this.autoList = autoList;
    }

    @Nullable
    public List<EntityAuto> getAutoList() {
        return autoList;
    }

    public void setAutoList(@Nullable List<EntityAuto> autoList) {
        this.autoList = autoList;
    }
}
