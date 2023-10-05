package tech.stark.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.repository.AssignmentRepository;
import tech.stark.webapp.security.SecurityService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentService {

    private final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);
    @Autowired
    private SecurityService securityService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AssignmentRepository assignmentRepository;

    public  List<Assignment> getAssignments(String id) {
        if(id == null){
            return StreamSupport.stream(
                    assignmentRepository.findAll().spliterator(),
                    false).collect(Collectors.toList());
        } else {
            Optional<Assignment> isAssignment = assignmentRepository.findById(id);
            return isAssignment.map(List::of).orElse(null);
        }

    }

    public Assignment save(Assignment assignment) {
        assignment.setUser_email(securityService.getUser().getUsername());
        String now = Instant.now().toString();
        assignment.setAssignment_created(now);
        assignment.setAssignment_updated(now);
        return assignmentRepository.save(assignment);
    }

    public Optional<Boolean> deleteAssigment(String id) {
        if(assignmentRepository.existsById(id)){
            Assignment oldAssignment = assignmentRepository.findById(id).get();
            if(oldAssignment.getUser_email().equals(securityService.getUser().getUsername())){
                assignmentRepository.deleteById(id);
                return Optional.of(true);
            } else {
                return null;
            }

        } else {
            return Optional.of(false);
        }
    }

    public Optional<Assignment> updateAssignment(String id, Assignment assignment) {
        if(assignmentRepository.existsById(id)){
            assignment.setId(id);
            Assignment oldAssignment = assignmentRepository.findById(id).get();
            if(oldAssignment.getUser_email().equals(securityService.getUser().getUsername())){
                assignment.setAssignment_created(oldAssignment.getAssignment_created());
                assignment.setUser_email(oldAssignment.getUser_email());
                assignment.setAssignment_updated(Instant.now().toString());
                assignmentRepository.save(assignment);
            } else {
                return null;
            }

            return Optional.of(assignment);
        } else {
            return Optional.empty();
        }
    }
}
