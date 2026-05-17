package server_2026_b.server.service;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.UserType;

import java.util.Locale;

//  סיסמה עבור כל משתמש: 1234
// שם משתמש מוצג ב DB

@Service
public class TestService {

    @Autowired
    private Persist persist;
    Faker faker = new Faker(new Locale("en", "US"));

    private final int NUMBER_OF_EMPLOYEES = 3;
    private final int NUMBER_OF_EMPLOYERS = 2;
    private final int NUMBER_OF_ADMINS = 1;
    private final int NUMBER_OF_WORKING_SITES = 3;

    public void addData() {
//        createEmployees();
//        createEmployers();
//        createWorkingSites();
        createAdmin();
    }

    private void createEmployees() {
        for (int i = 1; i <= NUMBER_OF_EMPLOYEES; i++) {
            User emp = new User();
            emp.setFirstName(faker.name().firstName());
            emp.setLastName(faker.name().lastName());
            emp.setPhone(faker.phoneNumber().cellPhone());
            emp.setUserType(UserType.EMPLOYEE);
            emp.setUsername(emp.getFirstName());
            emp.setPassword(generateHashPassword(emp.getUsername(), "1234"));
            emp.setEmail("employee-" + emp.getFirstName() + "@test.com");

            persist.save(emp);
        }
    }

    private void createEmployers() {
        for (int i = 1; i <= NUMBER_OF_EMPLOYERS; i++) {
            User emp = new User();
            emp.setFirstName(faker.name().firstName());
            emp.setLastName(faker.name().lastName());
            emp.setPhone(faker.phoneNumber().cellPhone());
            emp.setUserType(UserType.EMPLOYER);
            emp.setUsername(emp.getFirstName());
            emp.setPassword(generateHashPassword(emp.getUsername(), "1234"));
            emp.setEmail("employer-" + emp.getFirstName() + "@test.com");

            persist.save(emp);
        }
    }

    private void createAdmin() {
        for (int i = 1; i <= NUMBER_OF_ADMINS; i++) {
            User emp = new User();
            emp.setFirstName(faker.name().firstName());
            emp.setLastName(faker.name().lastName());
            emp.setPhone(faker.phoneNumber().cellPhone());
            emp.setUserType(UserType.ADMIN);
            emp.setUsername(emp.getFirstName());
            emp.setPassword(generateHashPassword(emp.getUsername(), "1234"));
            emp.setEmail("employer-" + emp.getFirstName() + "@test.com");

            persist.save(emp);
        }
    }

    private void createWorkingSites() {
        for (int i = 1; i <= NUMBER_OF_WORKING_SITES; i++) {
            WorkingSite workingSite = new WorkingSite();
            workingSite.setName(faker.company().name());
            workingSite.setAddress(faker.address().city());
            workingSite.setLongitude(Double.parseDouble(faker.address().longitude()));
            workingSite.setLatitude(Double.parseDouble(faker.address().latitude()));

            persist.save(workingSite);
        }
    }

    private String generateHashPassword(String username, String password) {
        return GenerateHash.hashMd5(username, password);
    }
}
