package com.example.seonoh2.smarttoliet01.model.toilets;

public class ToiletsResults {
  private int roomCount;
  private String createdAt;
  private int toiletIdx;
  private String address;
  private double latitude;
  private String name;
  private String beaconId;
  private Object type;
  private Object ipAddr;
  private double longitude;

  public int getRoomCount() {
    return this.roomCount;
  }

  public void setRoomCount(int roomCount) {
    this.roomCount = roomCount;
  }

  public String getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public int getToiletIdx() {
    return this.toiletIdx;
  }

  public void setToiletIdx(int toiletIdx) {
    this.toiletIdx = toiletIdx;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBeaconId() {
    return this.beaconId;
  }

  public void setBeaconId(String beaconId) {
    this.beaconId = beaconId;
  }

  public Object getType() {
    return this.type;
  }

  public void setType(Object type) {
    this.type = type;
  }

  public Object getIpAddr() {
    return this.ipAddr;
  }

  public void setIpAddr(Object ipAddr) {
    this.ipAddr = ipAddr;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
