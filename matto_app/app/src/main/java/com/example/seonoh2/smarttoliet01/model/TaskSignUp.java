package com.example.seonoh2.smarttoliet01.model;

/**
 * Created by tylenol on 2016. 11. 1..
 */

public class TaskSignUp {
  String id, pwd, ndPwd, phoneNum, gender, auth;

  public TaskSignUp(String id, String pwd, String ndPwd, String phoneNum, String gender, String auth) {

    this.id = id;
    this.pwd = pwd;
    this.ndPwd = ndPwd;
    this.phoneNum = phoneNum;
    this.gender = gender;
    this.auth = auth;

  }
}
