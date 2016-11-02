package com.example.seonoh2.smarttoliet01.model.toilets;

public class NearbyToilets {
  private String reason;
  private NearbyToiletsResultData resultData;
  private String status;

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public NearbyToiletsResultData getResultData() {
    return this.resultData;
  }

  public void setResultData(NearbyToiletsResultData resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
