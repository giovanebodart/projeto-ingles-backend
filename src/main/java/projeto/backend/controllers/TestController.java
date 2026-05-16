package projeto.backend.controllers;
import org.springframework.stereotype.Component;

import projeto.backend.services.CerfLoader;
import projeto.backend.services.StopWordLoader;

@Component
public class TestController {
    
    private final StopWordLoader stopWordLoader;
    private final CerfLoader cerfLoader;

    public TestController(StopWordLoader stopWordLoader, CerfLoader cerfLoader) {
        this.stopWordLoader = stopWordLoader;
        this.cerfLoader = cerfLoader;
    }

    public void test() {

    }
}
