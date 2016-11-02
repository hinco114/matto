package com.example.seonoh2.smarttoliet01.data;

import java.util.List;

/**
 * Created by 선오 on 2016-09-09.
 */
public class LoginResult {
    private String status;
    private String reason;
    private String resultData;
    private List<LoginToken> loginTokens;

    public List<LoginToken> getLoginTokens() {
        return loginTokens;
    }

    public void setLoginTokens(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
