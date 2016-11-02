package com.example.seonoh2.smarttoliet01.model.login;

public class LoginCheck {
  private int exp;
  private int iat;
  private LoginCheckInfo info;

  public int getExp() {
    return this.exp;
  }

  public void setExp(int exp) {
    this.exp = exp;
  }

  public int getIat() {
    return this.iat;
  }

  public void setIat(int iat) {
    this.iat = iat;
  }

  public LoginCheckInfo getInfo() {
    return this.info;
  }

  public void setInfo(LoginCheckInfo info) {
    this.info = info;
  }
}
