package com.gradea;

public class ControllerFactory {

  private static AuthController authControllerInstance;

  public static AuthController getAuthControllerInstance() {
    if (authControllerInstance == null) {
      authControllerInstance = new AuthController();
    }
    return authControllerInstance;
  }
}
