package com.kadri.resilience4j.web;

import com.kadri.resilience4j.util.CbInspector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InspectorController {

    private final CbInspector inspector;

    public InspectorController(CbInspector inspector) {
        this.inspector = inspector;
    }

    @GetMapping("/inspect/cb/{name}")
    public String getCbState(@PathVariable String name){
        return inspector.getState(name);
    }
}
