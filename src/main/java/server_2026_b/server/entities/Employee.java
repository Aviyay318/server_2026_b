package server_2026_b.server.entities;

import server_2026_b.server.utils.UserType;

public class Employee extends User {


    public Employee() {
    }

    public Employee(String username, String firstName, String lastName, String password, String phone, String email, UserType role) {
        super(username, firstName, lastName, password, phone, email, role);
    }
}

