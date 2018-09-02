package pro.kinect.taxi.rest;

import com.google.gson.annotations.SerializedName;

public class RestAutoModel  {

    @SerializedName("orderid")
    private int orderID;

    @SerializedName("statusforweborder")
    private int statusForWebOrder;

    @SerializedName("year")
    private String year;

    @SerializedName("status")
    private String status;

    @SerializedName("autotariffclassid")
    private String autoTariffClassId;

    @SerializedName("autocallsign")
    private String autoCallSign;

    @SerializedName("callsignid")
    private String callSignId;

    @SerializedName("statenumber")
    private String stateNumber;

    @SerializedName("carbrand")
    private String carBrand;

    @SerializedName("carcolor")
    private String carColor;

    @SerializedName("drivename")
    private String driverName;

    @SerializedName("geox")
    private String geoX;

    @SerializedName("geoy")
    private String geoY;

    @SerializedName("driverphone")
    private String driverPhone;

    @SerializedName("autotariffclassname")
    private String autoTariffClassName;

    public int getOrderID() {
        return orderID;
    }

    public int getStatusForWebOrder() {
        return statusForWebOrder;
    }

    public String getYear() {
        return year;
    }

    public String getStatus() {
        return status;
    }

    public String getAutoTariffClassId() {
        return autoTariffClassId;
    }

    public String getAutoCallSign() {
        return autoCallSign;
    }

    public String getCallSignId() {
        return callSignId;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarColor() {
        return carColor;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getGeoX() {
        return geoX;
    }

    public String getGeoY() {
        return geoY;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public String getAutoTariffClassName() {
        return autoTariffClassName;
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
}

// ВА8986ВО
// или
// Дачиа Сандеро, carColor = Синий //32.2867, geoY = 48.5289 (статус 1)
// Шевроле Авео, carColor = Черный, driverName = Лакатош Вадим Иванович, geoX = 32.2931, geoY = 48.5288 (статус 6)
// 32.2364, geoY = 48.5037
