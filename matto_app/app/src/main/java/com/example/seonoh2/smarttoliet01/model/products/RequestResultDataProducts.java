package com.example.seonoh2.smarttoliet01.model.products;

public class RequestResultDataProducts {
  private int productIdx;
  private int price;
  private String name;
  private String description;
  private int stock;
  private String imgSrc;

  public int getProductIdx() {
    return this.productIdx;
  }

  public void setProductIdx(int productIdx) {
    this.productIdx = productIdx;
  }

  public int getPrice() {
    return this.price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getStock() {
    return this.stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public String getImgSrc() {
    return this.imgSrc;
  }

  public void setImgSrc(String imgSrc) {
    this.imgSrc = imgSrc;
  }
}
