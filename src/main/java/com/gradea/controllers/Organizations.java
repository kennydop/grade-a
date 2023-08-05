package com.gradea.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gradea.models.Organization;
import com.gradea.models.Response;

public class Organizations {
  private static final Organizations instance = new Organizations();
  private static Connection dbConnection;

  private Organizations() {
    try {
      this.dbConnection = DB.getInstance().getConnection();
    } catch (Exception e) {
      System.out.println("Error connecting to database: " + e.getMessage());
    }
  }

  public static Organizations getInstance() {
    return instance;
  }

  public Response createOrganization(String name, int created_by, String support_email) {
    return this._createOrganization(name, created_by, support_email);
  }

  public Response createOrganization(String name, String support_email) {
    return this._createOrganization(name, Session.getInstance().getCurrentUser().getID(), support_email);
  }

  private Response _createOrganization(String name, int created_by, String support_email) {
    String sql = "INSERT INTO organizations (unique_code, name, created_by, support_email) VALUES (?, ?, ?, ?)";

    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
      final String uc = generateUniqueCode();
      pstmt.setString(1, uc);
      pstmt.setString(2, name.strip());
      pstmt.setInt(3, 1);
      pstmt.setString(4, support_email.strip());
      pstmt.executeUpdate();
      System.out.println("Inserted " + name + " organization.");
      joinOrganization(uc);
      return new Response(true, "Organization created successfully.", uc);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return new Response(false, "Error creating organization.");
  }

  public Response joinOrganization(String uniqueCode) {
    String sql = "INSERT INTO organization_users (org_id, user_id) VALUES ((SELECT id FROM organizations WHERE unique_code = ?), ?)";
    int userId = Session.getInstance().getCurrentUser().getID();
    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setString(1, uniqueCode);
      pstmt.setInt(2, userId);
      pstmt.executeUpdate();
      System.out.println("User with id " + userId + " joined the organization with unique code " + uniqueCode);
      return new Response(true, "Joined organization successfully.");
    } catch (SQLException e) {
      System.out.println(e.getErrorCode());
      System.out.println(e.getMessage());
      return new Response(false,
          e.getErrorCode() == 1048 ? "No such organization"
              : "Error joining organization.",
          e);
    }
  }

  public Response getUserOrganizations() {
    String sql = "SELECT o.* FROM organizations o " +
        "INNER JOIN organization_users ou ON o.id = ou.org_id " +
        "WHERE ou.user_id = ?";

    int userId = Session.getInstance().getCurrentUser().getID();
    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();

      List<Organization> organizations = new ArrayList<>();

      while (rs.next()) {
        Organization org = new Organization(rs.getInt("id"), rs.getString("name"), rs.getString("unique_code"),
            rs.getInt("created_by"), rs.getString("support_email"));
        organizations.add(org);
      }

      return new Response(true, "Fetched organizations successfully.", organizations);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return new Response(false, "Error fetching organizations.");
    }
  }

  public Response getUserCreatedOrganizations() {
    String sql = "SELECT * FROM organizations WHERE created_by = ?";

    int userId = Session.getInstance().getCurrentUser().getID();
    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
      pstmt.setInt(1, userId);
      ResultSet rs = pstmt.executeQuery();

      List<Organization> organizations = new ArrayList<>();

      while (rs.next()) {
        Organization org = new Organization(rs.getInt("id"), rs.getString("name"), rs.getString("unique_code"),
            rs.getInt("created_by"), rs.getString("support_email"));
        organizations.add(org);
      }

      return new Response(true, "Fetched organizations successfully.", organizations);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return new Response(false, "Error fetching organizations.");
    }
  }

  public static String generateUniqueCode() {
    String uniqueCode = UUID.randomUUID().toString();
    return uniqueCode.substring(0, 5);
  }
}
