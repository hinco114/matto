package com.example.seonoh2.smarttoliet01.model.toilets;

public class Toilets {
  private Object reason;
  private ToiletsResults[] toilets;
  private int lastOffset;
  private String status;

  public Object getReason() {
    return this.reason;
  }

  public void setReason(Object reason) {
    this.reason = reason;
  }

  public ToiletsResults[] getToilets() {
    return this.toilets;
  }

  public void setToilets(ToiletsResults[] toilets) {
    this.toilets = toilets;
  }

  public int getLastOffset() {
    return this.lastOffset;
  }

  public void setLastOffset(int lastOffset) {
    this.lastOffset = lastOffset;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
