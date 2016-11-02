package com.example.seonoh2.smarttoliet01.data;

/**
 * Created by 선오 on 2016-09-16.
 */
public class Product {

    private int productIdx;  //상품인덱스
    private String name; //상품 이름
    private String price; // 상품 가격
    private String imgSrc; // 상품 이미지
    private String description; // 상품 설명


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getProductIdx() {
        return productIdx;
    }

    public void setProductIdx(int productIdx) {
        this.productIdx = productIdx;
    }

}


