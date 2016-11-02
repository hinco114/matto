package com.example.seonoh2.smarttoliet01.data;

import java.util.List;

/**
 * Created by 선오 on 2016-09-15.
 */
public class AitemResult {
    private String status;
    private String reason;
    private int length;
    private String lastOffset;
    private List<Product> products;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(String lastOffset) {
        this.lastOffset = lastOffset;
    }

//    data.product.get(1).getname 1번에해당하는 배열값 접근

}

