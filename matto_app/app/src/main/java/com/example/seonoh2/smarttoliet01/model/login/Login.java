package com.example.seonoh2.smarttoliet01.model.login;

public class Login {
  private Object reason;
  private LoginResultData resultData;
  private String status;

  public Object getReason() {
    return this.reason;
  }

  public void setReason(Object reason) {
    this.reason = reason;
  }

  public LoginResultData getResultData() {
    return this.resultData;
  }

  public void setResultData(LoginResultData resultData) {
    this.resultData = resultData;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
