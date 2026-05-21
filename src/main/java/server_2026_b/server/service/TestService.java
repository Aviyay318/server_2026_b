package server_2026_b.server.service;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server_2026_b.server.entities.User;
import server_2026_b.server.entities.WorkingSite;
import server_2026_b.server.utils.GenerateHash;
import server_2026_b.server.utils.IdValidator;
import server_2026_b.server.utils.UserType;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

//  סיסמה עבור כל משתמש: 1234
//  שם משתמש (תז) מוצג ב DB

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
            emp.setPersonalId(generateValidIsraeliId());
            emp.setPassword(generateHashPassword(emp.getPersonalId(), "1234"));
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
            emp.setPersonalId(generateValidIsraeliId());
            emp.setPassword(generateHashPassword(emp.getPersonalId(), "1234"));
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
            emp.setPersonalId(generateValidIsraeliId());
            emp.setPassword(generateHashPassword(emp.getPersonalId(), "1234"));
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

    private String generateHashPassword(String personalId, String password) {
        return GenerateHash.hashMd5(personalId, password);
    }

    private String generateValidIsraeliId() {
        // יצירה של 8 ספרות אקראיות + ספרת ביקורת מחושבת כך שסך ה-Luhn יתחלק ב-10
        StringBuilder base = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            base.append(ThreadLocalRandom.current().nextInt(10));
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int digit = base.charAt(i) - '0';
            int temp = ((i % 2) + 1) * digit;
            sum += (temp > 9) ? (temp / 10 + temp % 10) : temp;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        String candidate = base.toString() + checkDigit;
        // הגנה כפולה: אם משהו השתבש, ננסה שוב
        return IdValidator.checkID(candidate) ? candidate : generateValidIsraeliId();
    }
}
