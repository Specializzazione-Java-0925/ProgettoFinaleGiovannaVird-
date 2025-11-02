package it.chronicle.demo.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.chronicle.demo.Models.CareerRequest;
import it.chronicle.demo.Models.Role;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Repositories.RoleRepository;
import it.chronicle.demo.Repositories.UserRepository;
import it.chronicle.demo.Services.CareerRequestService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;









@Controller
@RequestMapping("/operations")
public class OperationController {
    
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CareerRequestService careerRequestService;

    @GetMapping("/career/request")
    public String careerRequestCreate(Model viewModel) {
        viewModel.addAttribute("title", "Inserisci la tua richiesta");
        viewModel.addAttribute("careerRequest", new CareerRequest());

        List<Role> roles = roleRepository.findAll();
        roles.removeIf(e->e.getName().equals("ROLE_USER"));
        viewModel.addAttribute("roles", roles);

        return "career/requestForm";
    }
    
    @PostMapping("/career/request/save")
    public String careerRequestStror(@ModelAttribute("careerRequest") CareerRequest careerRequest, Principal principal, RedirectAttributes redirectAttributes) {
        User user= userRepository.findByEmail(principal.getName());

        if(careerRequestService.isRoleAlReadyAssigned(user, careerRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Sei già assegnato a questo ruolo!");
            return "redirecy:/";
        }
        
        careerRequestService.save(careerRequest, user);
        redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");
        return "redirect:/";
    }
    
    @GetMapping("/career/request/detail/{id}")
    public String careerRequestDetail(@PathVariable("id") Long id , Model viewModel) {
        viewModel.addAttribute("title", "Dettaglio richiesta");
        viewModel.addAttribute("request", careerRequestService.find(id));
        return "career/requestDetail";
    }
    
    @PostMapping("/career/request/accept/{requestId}")
    public String careerRequestAccept(@PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        careerRequestService.careerAccept(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Ruolo abilitato per l'utente ");
        return "redirect:/admin/dashboard";
    }
    
}
