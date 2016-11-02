package com.example.seonoh2.smarttoliet01.model.toilets;

public class NearbyToiletsResultDataToilets {
  private int toiletIdx;
  private float distance;
  private String name;

  public int getToiletIdx() {
    return this.toiletIdx;
  }

  public void setToiletIdx(int toiletIdx) {
    this.toiletIdx = toiletIdx;
  }

  public double getDistance() {
    return this.distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
