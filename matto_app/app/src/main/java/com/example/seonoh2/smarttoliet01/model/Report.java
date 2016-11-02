package com.example.seonoh2.smarttoliet01.model;

public class Report {
  private String reason;
  private Object resultData;
  private String status;

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Object getResultData() {
    return this.resultData;
  }

  public void setResultData(Object resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
