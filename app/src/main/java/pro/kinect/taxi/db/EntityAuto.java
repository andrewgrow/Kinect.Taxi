package pro.kinect.taxi.db;

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import pro.kinect.taxi.App;
import pro.kinect.taxi.rest.AutoListResponse;
import pro.kinect.taxi.rest.BaseResponse;

@Entity
public class EntityAuto {

    @Ignore
    private static final String TAG = EntityAuto.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private long id;

    @SerializedName("orderid")
    @ColumnInfo(name = "orderid")
    private int orderID;

    @SerializedName("statusforweborder")
    @ColumnInfo(name = "statusforweborder")
    private int statusForWebOrder;

    @SerializedName("year")
    @ColumnInfo(name = "year")
    private String year;

    @SerializedName("status")
    @ColumnInfo(name = "status")
    private String status;

    @SerializedName("autotariffclassid")
    @ColumnInfo(name = "autotariffclassid")
    private String autoTariffClassId;

    @SerializedName("autocallsign")
    @ColumnInfo(name = "autocallsign")
    private String autoCallSign;

    @SerializedName("callsignid")
    @ColumnInfo(name = "callsignid")
    private String callSignId;

    @SerializedName("statenumber")
    @ColumnInfo(name = "statenumber")
    private String stateNumber;

    @SerializedName("carbrand")
    @ColumnInfo(name = "carbrand")
    private String carBrand;

    @SerializedName("carcolor")
    @ColumnInfo(name = "carcolor")
    private String carColor;

    @SerializedName("drivename")
    @ColumnInfo(name = "drivename")
    private String driverName;

    @SerializedName("geox")
    @ColumnInfo(name = "geox")
    private String geoX;

    @SerializedName("geoy")
    @ColumnInfo(name = "geoy")
    private String geoY;

    @SerializedName("driverphone")
    @ColumnInfo(name = "driverphone")
    private String driverPhone;

    @SerializedName("autotariffclassname")
    @ColumnInfo(name = "autotariffclassname")
    private String autoTariffClassName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getStatusForWebOrder() {
        return statusForWebOrder;
    }

    public void setStatusForWebOrder(int statusForWebOrder) {
        this.statusForWebOrder = statusForWebOrder;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAutoTariffClassId() {
        return autoTariffClassId;
    }

    public void setAutoTariffClassId(String autoTariffClassId) {
        this.autoTariffClassId = autoTariffClassId;
    }

    public String getAutoCallSign() {
        return autoCallSign;
    }

    public void setAutoCallSign(String autoCallSign) {
        this.autoCallSign = autoCallSign;
    }

    public String getCallSignId() {
        return callSignId;
    }

    public void setCallSignId(String callSignId) {
        this.callSignId = callSignId;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getGeoX() {
        return geoX;
    }

    public void setGeoX(String geoX) {
        this.geoX = geoX;
    }

    public String getGeoY() {
        return geoY;
    }

    public void setGeoY(String geoY) {
        this.geoY = geoY;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getAutoTariffClassName() {
        return autoTariffClassName;
    }

    public void setAutoTariffClassName(String autoTariffClassName) {
        this.autoTariffClassName = autoTariffClassName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
//                .append("orderID = ").append(getOrderID())
//                .append(", getStatusForWebOrder = ").append(getStatusForWebOrder())
//                .append(", year = ").append(getYear())
                .append("status = ").append(getStatus())
                .append(getReadableStatus(getStatus()))
                .append(", stateNumber = ").append(getStateNumber())
                .append(", carBrand = ").append(getCarBrand())
                .append(", carColor = ").append(getCarColor())
                .append(", driverName = ").append(getDriverName())
                .append(", geoX = ").append(getGeoX())
                .append(", geoY = ").append(getGeoY())
                .append(", driverPhone = ").append(getDriverPhone())
                .append(", autoTariffClassId = ").append(getAutoTariffClassId())
                .append(", autoTariffClassName = ").append(getAutoTariffClassName())
                .append(", autoCallSign = ").append(getAutoCallSign())
                .append(", callSignId = ").append(getCallSignId())
                ;

        return builder.toString();
    }

    private String getReadableStatus(String status) {
        return "0".equals(status) ? " (свободен)" : "";
    }

    @SuppressLint("CheckResult")
    public static void saveNewAuto(List<EntityAuto> autoList, Emitter<Boolean> observer) {
        Observable.fromCallable(() -> {
                    Log.d(TAG, "Browsing the list of cars that got from the server ...");
                    DaoAuto dao = App.getDatabase().daoAuto();
                    int existAuto = 0;
                    int newAuto = 0;
                    for (EntityAuto restAuto : autoList) {
                        EntityAuto dbAuto = dao.getAutoByNumber(restAuto.getStateNumber());
                        if (dbAuto == null) {
                            long id = dao.insert(restAuto);
                            Log.d(TAG, "create new auto! " + id + ", " + restAuto.getStateNumber());
                            newAuto++;
                        } else {
                            existAuto++;
                        }
                    }

                    Log.d(TAG, "newAuto = " + newAuto + ", exist = " + existAuto);

                    return dao.getAllAuto();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (observer != null) {
                        Log.d(TAG, "Work with the DB was successful!");
                        observer.onComplete();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static void returnAllAutoFromDB(@BaseResponse.Status int responseStatus,
                                            DisposableObserver<AutoListResponse> activityObserver) {
        String status = "INTERNET FAILURE";
        if (BaseResponse.SUCCESS == responseStatus) {
            status = "INTERNET SUCCESS";
        } else if (BaseResponse.LOCAL_RESPONSE == responseStatus) {
            status = "LOCAL DATA";
        }

        Log.d(TAG, "returnAllAutoFromDB -> "
                + "status = " + status);
        Observable.fromCallable(() -> App.getDatabase().daoAuto().getAllAuto())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (activityObserver != null) {
                        AutoListResponse response = new AutoListResponse(responseStatus, list);
                        activityObserver.onNext(response);
                    }
                });
    }
}