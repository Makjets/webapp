package tech.stark.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.repository.AssignmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentService {

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
        return assignmentRepository.save(assignment);
    }

    public boolean deleteAssigment(String id) {
        if(assignmentRepository.existsById(id)){
            assignmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Assignment> updateAssignment(String id, Assignment assignment) {
        if(assignmentRepository.existsById(id)){
            assignment.setId(id);
            assignmentRepository.save(assignment);
            return Optional.of(assignment);
        } else {
            return Optional.empty();
        }
    }
}
