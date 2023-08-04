package com.gradea.models;

public class Response {

  private boolean success;
  private String message;
  private Object data;

  public Response(boolean success, String message) {
    this.success = success;
    this.message = message;
    this.data = null;
  }

  public Response(boolean success, String message, Object data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public boolean getSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
