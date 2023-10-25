package tech.stark.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import tech.stark.webapp.controller.error.JsonFormatException;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.service.AssignmentService;
import tech.stark.webapp.service.ValidationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/assignments")
public class AssignmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);
    private static final String CREATE_REQUEST_SCHEMA = "assignment_schema.json";

    @Value("${application.config.min-points}")
    private int min;

    @Value("${application.config.max-points}")
    private int max;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private ValidationService validationService;
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
    public ResponseEntity<Assignment> createAssignment(@RequestBody String createRequest,
                                                       HttpServletRequest request) {

        JsonNode requestJson = validationService.validateJson(createRequest, CREATE_REQUEST_SCHEMA);
        ObjectMapper objectMapper = new ObjectMapper();
        Assignment assignment = null;
        try {
            assignment = objectMapper.treeToValue(requestJson, Assignment.class);
        } catch (JsonProcessingException e) {
            throw new JsonFormatException(e.getMessage());
        }
        if(request.getQueryString()!=null){
            return ResponseEntity.badRequest().build();
        } else if (assignment.getPoints()<min || assignment.getPoints()>max){
            return ResponseEntity.badRequest().build();
        } else if(assignment.getName()==null || assignment.getPoints()==null || assignment.getDeadline()==null || assignment.getNum_of_attempts()==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.save(assignment));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable String id,
                                                       HttpServletRequest request,
                                                       @RequestBody @Nullable String body) {
        if(request.getQueryString()!=null || body!=null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Boolean> isDeleted = assignmentService.deleteAssigment(id);
        if(isDeleted ==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else if(!isDeleted.get()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable String id,
                                                       @RequestBody String createRequest,
                                                       HttpServletRequest request) {
        JsonNode requestJson = validationService.validateJson(createRequest, CREATE_REQUEST_SCHEMA);
        ObjectMapper objectMapper = new ObjectMapper();
        Assignment assignment = null;
        try {
            assignment = objectMapper.treeToValue(requestJson, Assignment.class);
        } catch (JsonProcessingException e) {
            throw new JsonFormatException(e.getMessage());
        }

        if(request.getQueryString()!=null){
            return ResponseEntity.badRequest().build();
        } else if (assignment.getPoints()<min || assignment.getPoints()>max){
            return ResponseEntity.badRequest().build();
        } else if(assignment.getName()==null || assignment.getPoints()==null || assignment.getDeadline()==null || assignment.getNum_of_attempts()==null){
            return ResponseEntity.badRequest().build();
        }
        Optional<Assignment> isAssignment = assignmentService.updateAssignment(id,assignment);
        if(isAssignment==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return isAssignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
