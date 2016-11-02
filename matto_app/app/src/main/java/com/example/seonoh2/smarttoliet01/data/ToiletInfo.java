package com.example.seonoh2.smarttoliet01.data;

/**
 * Created by 선오 on 2016-09-17.
 */
public class ToiletInfo {
    private int toiletIdx;
    private String name;
    private float longitude;
    private float latitude;
    private String address;
    private int roomCount;
    private String beaconId;
    private String createdAt;

    public int getToiletIdx() {
        return toiletIdx;
    }

    public void setToiletIdx(int toiletIdx) {
        this.toiletIdx = toiletIdx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
