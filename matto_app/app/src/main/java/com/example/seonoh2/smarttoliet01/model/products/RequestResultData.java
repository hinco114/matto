package com.example.seonoh2.smarttoliet01.model.products;

public class RequestResultData {
  private int lastOffset;
  private RequestResultDataProducts[] products;

  public int getLastOffset() {
    return this.lastOffset;
  }

  public void setLastOffset(int lastOffset) {
    this.lastOffset = lastOffset;
  }

  public RequestResultDataProducts[] getProducts() {
    return this.products;
  }

  public void setProducts(RequestResultDataProducts[] products) {
    this.products = products;
  }
}
