package com.kadri.resilience4j.external;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external")
public class ExternalStubController {

    @GetMapping("/ok")
    public ResponseEntity<String> ok(){
        return ResponseEntity.ok("external success");
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("external failure");
    }

    @GetMapping("/slow")
    public ResponseEntity<String> slow(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        return ResponseEntity.ok("slow external response");
    }
}
