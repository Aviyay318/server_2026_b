package server_2026_b.server.responses;


import server_2026_b.server.entities.User;

import java.util.List;

public class AdminResponse extends BasicResponse {

    private List<User> users; //מחזיר רשימת עובדים או מעסיקים

    public AdminResponse() {
    }

    public AdminResponse(boolean success , Integer errorCode, String message){
        super(success , errorCode);
    }

    public AdminResponse(boolean success, Integer errorCode, List<User> users){
        super(success , errorCode);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
