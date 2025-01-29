package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.ArrayList;

public class MessageService {
    private MessageDAO messageDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message sendMessage(Message message){
        return messageDAO.sendMessage(message);
    }

    public ArrayList<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(Integer messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(Integer messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(Integer messageId, String message){
        return messageDAO.updateMessageById(messageId, message);
    }

    public ArrayList<Message> getMessages(Integer accountId){
        return messageDAO.getMessagesById(accountId);
    }
}
