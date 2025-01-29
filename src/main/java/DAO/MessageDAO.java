package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message sendMessage(Message message){
        java.sql.Connection connection = ConnectionUtil.getConnection();
    try{
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());
        preparedStatement.executeUpdate();
        return message;
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
    }

    public ArrayList<Message> getAllMessages(){
        java.sql.Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();
        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(Integer messageId){
        java.sql.Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(Integer messageID){
        java.sql.Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageID);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessageById(Integer messageId, String message){
        java.sql.Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, messageId);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Message> getMessagesById(Integer accountId){
        java.sql.Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message newMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                messages.add(newMessage);
            }
            return messages;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
