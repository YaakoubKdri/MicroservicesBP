package com.kadri.resilience4j.web;

import com.kadri.resilience4j.util.CbInspector;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "inspect", description = "API to inspect the state of the circuit breaker.")
public class InspectorController {

    private final CbInspector inspector;

    public InspectorController(CbInspector inspector) {
        this.inspector = inspector;
    }

    @GetMapping("/inspect/cb/{name}")
    @Operation(summary = "get circuit breaker state")
    public String getCbState(@PathVariable String name){
        return inspector.getState(name);
    }
}
