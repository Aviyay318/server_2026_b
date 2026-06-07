package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.*;
import server_2026_b.server.requests.EmployeeConstraintRequest;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.responses.EmployeeConstraintsResponse;
import server_2026_b.server.service.EmployeeConstraintsService;

import java.util.List;

@RestController
@RequestMapping("/employee-constraints")
public class EmployeeConstraintsController {

    private final EmployeeConstraintsService employeeConstraintsService;

    public EmployeeConstraintsController(EmployeeConstraintsService employeeConstraintsService) {
        this.employeeConstraintsService = employeeConstraintsService;
    }

    @PostMapping("/save-constraints")
    public BasicResponse saveConstraints( // שמירת רשימת אילוצים של עובד
                                          @CookieValue(value = "accessToken", required = false) String token,
                                          @RequestBody EmployeeConstraintRequest request) { // request = {employee_id, shift_id, available, comment, date} without auto-increment id
        // אבל בדיקה פנימית שהת״ז שמגיע בריקווסט מהלקוח שווה לת״ז העובד לפי הטוקן
        return employeeConstraintsService.saveConstraints(token, request); // נכניס את הריקווסטים (אילוצים) עם בדיקת הת״ז עם הטוקן לטבלה
        // יחזיר אם הצליח או נכשל בתוך המתודה בסרוויס
    }

    @GetMapping("/get-all")
    public EmployeeConstraintsResponse getAllConstraints( // כל האילוצים עבור המעסיק
                                                          @CookieValue(value = "accessToken", required = false) String token) {
        return employeeConstraintsService.getAllConstraints(token);
    }
}
