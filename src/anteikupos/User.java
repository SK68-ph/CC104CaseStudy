/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anteikupos;

/**
 *
 * @author Admin
 */
public class User {
    private String _username;
    private String _fullname;
    private user_role _role;
    
    
    public enum user_role{
        admin,
        manager,
        cashier
    }
    public User(){}
    public User(String username,String fullname){
        setUsername(username);
        setFullname(fullname);
    }
    
    
    public String getUsername() { return this._username;}
    public String getFullname() { return this._fullname;}
    public user_role getUserRole() { return this._role;}
    
    public void setUsername(String username) { this._username = (username);}
    public void setFullname(String fullname) { this._fullname = (fullname);}
    public void setUseRole(int role) { 
        switch(role){
            case 1:
                this._role = user_role.admin;
                break;
            case 2:
                this._role = user_role.manager;
                break;
            case 3:
                this._role = user_role.cashier;
                break;
        }
    }
}
