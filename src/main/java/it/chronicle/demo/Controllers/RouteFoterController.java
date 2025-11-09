package it.chronicle.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class RouteFoterController {
    
    @GetMapping("/about")
    public String showAboutPage() {
        return "RouteFooter/about"; 
    }

    
}
    

