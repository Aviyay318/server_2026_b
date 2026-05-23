package server_2026_b.server.responses;


import java.util.List;

public class AdminResponse extends BasicResponse {

    private List<UserDTO> users;

    public AdminResponse() {
    }

    public AdminResponse(boolean success , Integer errorCode, String message){
        super(success , errorCode);
    }

    public AdminResponse(boolean success, Integer errorCode, List<UserDTO> users){
        super(success , errorCode);
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
