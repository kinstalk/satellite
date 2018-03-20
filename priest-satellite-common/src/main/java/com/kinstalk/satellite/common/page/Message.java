
package com.kinstalk.satellite.common.page;

/**
 * Created by mayan on 14-11-5.
 */
@SuppressWarnings("serial")
public class Message implements java.io.Serializable {
    private int status;// 编码
    private ErrorMessage error;// 结果
    private Object result;// 数据
    private String cookie;

    public Message() {
        this.status = 0;
    }

    public Message(int status, ErrorMessage error) {
        this.status = status;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    @SuppressWarnings("unchecked")
    public <E> E getResult() {
        return (E) result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
