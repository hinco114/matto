package com.example.seonoh2.smarttoliet01.model.products;

public class Request {
  private String reason;
  private RequestResultData resultData;
  private String status;

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public RequestResultData getResultData() {
    return this.resultData;
  }

  public void setResultData(RequestResultData resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
