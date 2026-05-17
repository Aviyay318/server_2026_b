package server_2026_b.server.responses;


import server_2026_b.server.entities.User;

import java.util.List;

public class AdminResponse extends BasicResponse {

    private List<AdminUserDTO> users;

    public AdminResponse() {
    }

    public AdminResponse(boolean success , Integer errorCode, String message){
        super(success , errorCode);
    }

    public AdminResponse(boolean success, Integer errorCode, List<AdminUserDTO> users){
        super(success , errorCode);
        this.users = users;
    }

    public List<AdminUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<AdminUserDTO> users) {
        this.users = users;
    }
}
