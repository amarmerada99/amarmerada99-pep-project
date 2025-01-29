package Service;

import DAO.SocialMediaDAO;
import Model.Account;

public class AccountService {
    private SocialMediaDAO AccountDAO;
    

    public AccountService(){
        AccountDAO = new SocialMediaDAO();
    }

    public AccountService(SocialMediaDAO AccountDAO){
        this.AccountDAO = AccountDAO;
    }

    public Account addAccount(Account account){
        Account addedAccount = AccountDAO.insertAccount(account);
        return addedAccount;
    }

    public Account login(String username, String password){
        Account account = new Account();
        account = AccountDAO.getAccountByUserPass(username, password);
        return account;
    }

    public Account getAccountByUser(Account account){
        return AccountDAO.getAccountByUser(account);
    }

    public Account getAccountById(int id){
        return AccountDAO.getAccountById(id);
    }
}
