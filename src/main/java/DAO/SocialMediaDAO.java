package DAO;

import Model.Account;
import Util.ConnectionUtil;
import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection;
import java.sql.*;

public class SocialMediaDAO{



public Account insertAccount(Account account){
    java.sql.Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "INSERT INTO account (username, password) VALUES (?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.executeUpdate();
        return account;
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

public Account getAccountById(int id){
    java.sql.Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return account;
        }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

public Account getAccountByUserPass(String username, String password){
    java.sql.Connection connection = ConnectionUtil.getConnection();
    try{
    String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, username);
    preparedStatement.setString(2, password);
    ResultSet rs = preparedStatement.executeQuery();
    if(rs.next()){
        Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        return account;
    }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}

public Account getAccountByUser(Account account){
    java.sql.Connection connection = ConnectionUtil.getConnection();
    Account foundAccount = new Account();
    try{
        String sql = "SELECT username FROM account WHERE username = ?;";
        PreparedStatement preparedStatement = ((java.sql.Connection) connection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.executeQuery();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if(pkeyResultSet.next()){
            foundAccount = account;
            System.out.println("found account");
            return foundAccount;
        }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}
}
