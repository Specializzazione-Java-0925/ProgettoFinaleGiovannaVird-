package it.chronicle.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.chronicle.demo.Models.CareerRequest;
import it.chronicle.demo.Models.Role;
import it.chronicle.demo.Models.User;
import it.chronicle.demo.Repositories.CareerRequestRepository;
import it.chronicle.demo.Repositories.RoleRepository;
import it.chronicle.demo.Repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class CareerRequestServiceImp implements CareerRequestService {
  
  @Autowired
  private CareerRequestRepository careerRequestRepository;
  @Autowired
  private EmailService emailService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  
  @Transactional
  @Override
  public boolean isRoleAlReadyAssigned(User user, CareerRequest careerRequest) {
    List<Long> allUserIds = careerRequestRepository.findAllUserIds();
    
    if(!allUserIds.contains(user.getId())){
      return false;
    }
    
    List<Long> request = careerRequestRepository.findByUserId(user.getId());
    return request.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
  }
  
  @Override
  public void save(CareerRequest careerRequest, User user) {
    careerRequest.setUser(user);
    careerRequest.setChecked(false);
    careerRequestRepository.save(careerRequest);
    
    emailService.sendSimpleEmail("admin@chronicle.com", "Richiesta per ruolo:" + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di " + user.getUsername());
  }
  
  @Override
  public void careerAccept(Long requestId) {
    // CareerRequest request = careerRequestRepository.findById(requestId).get();
    CareerRequest request = careerRequestRepository.findById(requestId)
    .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

    
    User user = request.getUser();
    Role role = request.getRole();
    
    // List<Role> rolesUser = user.getRoles();
    // Role newRole = roleRepository.findByName(role.getName());
    // rolesUser.add(newRole);
    
    List<Role> rolesUser = user.getRoles();
    rolesUser.clear(); 
    rolesUser.add(roleRepository.findByName(role.getName())); 
    userRepository.save(user);
    
    
    user.setRoles(rolesUser);
    userRepository.save(user);
    request.setChecked(true);
    careerRequestRepository.save(request);
    
    emailService.sendSimpleEmail(user.getEmail(), "Ruolo abilitato", "La tua richiesta di collaborazione è stata accettata! ");
  }
  
  @Override
  public CareerRequest find(Long id) {
    return careerRequestRepository.findById(id).get();
  }
  
  
}
