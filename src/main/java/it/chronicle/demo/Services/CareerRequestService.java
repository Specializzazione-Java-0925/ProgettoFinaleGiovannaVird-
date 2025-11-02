package it.chronicle.demo.Services;

import it.chronicle.demo.Models.CareerRequest;
import it.chronicle.demo.Models.User;

public interface CareerRequestService {
    boolean isRoleAlReadyAssigned(User user, CareerRequest careerRequest);
    void save(CareerRequest careerRequest, User user);
    void careerAccept(Long requestId);
    CareerRequest find(Long id);
}
