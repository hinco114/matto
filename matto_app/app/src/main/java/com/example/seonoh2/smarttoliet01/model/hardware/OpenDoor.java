package com.example.seonoh2.smarttoliet01.model.hardware;

public class OpenDoor {
  private String reason;
  private OpenDoorResultData resultData;
  private String status;

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public OpenDoorResultData getResultData() {
    return this.resultData;
  }

  public void setResultData(OpenDoorResultData resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
