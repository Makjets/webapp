package tech.stark.webapp.controller;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stark.webapp.models.Assignment;

import java.util.List;

@RestController
@RequestMapping("/v1/assignments")
public class AssignmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

    @GetMapping(path = "/{id}")
    public HttpEntity<List<Assignment>> getAssignments(@PathVariable @Nullable String id) {
        LOGGER.info("AssignmentController.getAssignments() called");
        return null;
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        LOGGER.info("AssignmentController.createAssignment(Assignment assignment) called");
        return null;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable String id) {
        LOGGER.info("id");
        return null;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable String id, @RequestBody Assignment assignment) {
        LOGGER.info(id);
        return null;
    }
}
