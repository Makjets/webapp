package tech.stark.webapp.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.repository.AssignmentRepository;
import tech.stark.webapp.service.AssignmentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/assignments")
public class AssignmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentService assignmentService;
    @GetMapping
    public HttpEntity<List<Assignment>> getAllAssignments(HttpServletRequest request,
                                                          @RequestBody @Nullable String body) {
        if(request.getQueryString()!=null || body!=null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(assignmentService.getAssignments(null));
    }

    @GetMapping(path = "/{id}")
    public HttpEntity<List<Assignment>> getAssignments(HttpServletRequest request,
                                                       @PathVariable String id,
                                                       @RequestBody @Nullable String body) {
        if(request.getQueryString()!=null || body!=null){
            return ResponseEntity.badRequest().build();
        }
        List<Assignment> assignments = assignmentService.getAssignments(id);
        if(assignments==null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(assignments);
        }
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment,
                                                       HttpServletRequest request) {
        if(request.getQueryString()!=null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(assignmentService.save(assignment));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable String id,
                                                       HttpServletRequest request,
                                                       @RequestBody @Nullable String body) {
        if(request.getQueryString()!=null || body!=null){
            return ResponseEntity.badRequest().build();
        }
        if(assignmentService.deleteAssigment(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable String id,
                                                       @RequestBody Assignment assignment,
                                                       HttpServletRequest request) {
        if(request.getQueryString()!=null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Assignment> isAssignment = assignmentService.updateAssignment(id,assignment);
        return isAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}