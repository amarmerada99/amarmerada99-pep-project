package Controller;

import java.sql.Connection;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;
import Service.AccountService;
import Service.MessageService;
import Util.ConnectionUtil;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registrationHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getMessageHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}",this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::userMessagesHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    


    /*create a new Account on the endpoint POST localhost:8080/register. The body will contain 
    a representation of a JSON Account, but will not contain an account_id.

    The registration will be successful if and only if the username is not blank, the password 
    is at least 4 characters long, and an Account with that username does not already exist. 
    If all these conditions are met, the response body should contain a JSON of the Account, 
    including its account_id. The response status should be 200 OK, which is the default. 
    The new account should be persisted to the database.
    If the registration is not successful, the response status should be 400. (Client error) */
    private void registrationHandler(Context context){
        String JSONString = context.body();
        ObjectMapper OM = new ObjectMapper();
        Account account = new Account();
        Account duplicateTest = accountService.getAccountByUser(account);
        try {
            account = OM.readValue(JSONString, Account.class);
            if(account.getPassword().length() < 4){
                System.out.println("password too short");
                context.status(400);
            }else if(account.getUsername() == null || account.getUsername() == ""){
                System.out.println("Username empty");
                context.status(400);
            }else if(duplicateTest != null){
                System.out.println("Duplicate Username");
                context.status(400);
            }else{
                if(accountService.addAccount(account) == null){
                    context.status(400);
                    System.out.println("Account addition failed");
                }else{
                    context.status(200);
                    context.json(account);
                }
                System.out.println("Else block");
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void loginHandler(Context context){
        String JSONString = context.body();
        ObjectMapper OM = new ObjectMapper();
        Account account = new Account();
        try{
            account = OM.readValue(JSONString, Account.class);
            if(accountService.login(account.getUsername(), account.getPassword()) == null){
                context.status(401);
            }else{
                context.status(200);
                context.json(account);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void postMessageHandler(Context context){
        ObjectMapper mapper = new ObjectMapper();
        Message postedMessage = null;
        try{
            Message message = mapper.readValue(context.body(), Message.class);
            if(message.getMessage_text().length() >0 && message.getMessage_text().length() <256){
                if(accountService.getAccountById(message.getPosted_by()) != null){
                    postedMessage = messageService.sendMessage(message);
                    context.json(postedMessage);
                }
            }    
            if(postedMessage != null){
                context.status(200);
            }else{
                context.status(400);
            }
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }

    private void getMessageHandler(Context context){
        ArrayList<Message> allMessages = messageService.getAllMessages();
        context.json(allMessages);
        context.status(200);
    }

    private void getMessageByIdHandler(Context context){
        String JSONString = context.pathParam("message_id");
        ObjectMapper OM = new ObjectMapper();
        try{
            int messageId = OM.readValue(JSONString, Integer.class);
            if(messageService.getMessageById(messageId)!= null){
                Message foundMessage = messageService.getMessageById(messageId);
                context.json(foundMessage);
            }
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        context.status(200);
    }

    private void deleteMessageByIdHandler(Context context){
        String JSONString = context.pathParam("message_id");
        ObjectMapper OM = new ObjectMapper();
        try{
            int messageId = OM.readValue(JSONString, Integer.class);
            if(messageService.getMessageById(messageId)!= null){
                Message foundMessage = messageService.getMessageById(messageId);
                context.json(foundMessage);
                messageService.deleteMessage(messageId);
            }
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        context.status(200);
    }

    private void updateMessageHandler(Context context){
        ObjectMapper mapper = new ObjectMapper();
        String param = context.pathParam("message_id");
        try{
            int id = mapper.readValue(param, Integer.class);
            String message = mapper.readValue(context.body(), Message.class).getMessage_text();
            if(message.length() > 0 && message.length() < 256){  
                if(messageService.getMessageById(id) != null){
                    messageService.updateMessage(id, message);
                    Message updatedMessage = messageService.getMessageById(id);
                    context.json(updatedMessage);
                    context.status(200);
                }else{
                    context.status(400);
                }
            }else{
                context.status(400);
            }

        }catch(JsonMappingException e){
            System.out.println(e.getMessage());
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
    }

    private void userMessagesHandler(Context context){
        String param = context.pathParam("account_id");
        ObjectMapper om = new ObjectMapper();
        try{
            int userId = om.readValue(param, Integer.class);
            context.json(messageService.getMessages(userId));
        }catch(JsonMappingException e){
            System.out.println(e.getMessage());
        }catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        context.status(200);
    }
}