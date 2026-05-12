package server_2026_b.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.responses.BasicResponse;
import server_2026_b.server.service.TestService;

import static server_2026_b.server.utils.Errors.ERROR_TEST_ADD_DATA_FAILED;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/add-data")
    public BasicResponse addDataTest() {
        try {
            testService.addData();
            return new BasicResponse(true,null);
        } catch (Exception e) {
            return new BasicResponse(false,ERROR_TEST_ADD_DATA_FAILED);
        }
    }
}
