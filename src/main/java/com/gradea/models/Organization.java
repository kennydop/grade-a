package com.gradea.models;

public class Organization {
  Integer id;
  String name;
  String uniqueCode;
  int createdBy;
  String supportEmail;

  public Organization(Integer id, String name, String uniqueCode, int createdBy, String supportEmail) {
    this.id = id;
    this.name = name;
    this.uniqueCode = uniqueCode;
    this.createdBy = createdBy;
    this.supportEmail = supportEmail;
  }

  public Organization(String name, String uniqueCode, int createdBy, String supportEmail) {
    this.id = null;
    this.name = name;
    this.uniqueCode = uniqueCode;
    this.createdBy = createdBy;
    this.supportEmail = supportEmail;
  }

  public Integer getID() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUniqueCode() {
    return uniqueCode;
  }

  public int getCreatedBy() {
    return createdBy;
  }

  public String getSupportEmail() {
    return supportEmail;
  }

}
