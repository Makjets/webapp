package tech.stark.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.stark.domain.Assignment;

import java.util.List;

@Controller("/v1/assignments")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class AssignmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

    @Get("/")
    public HttpResponse<List<Assignment>> getAllAssignments() {
        LOGGER.info("AssignmentController.index() called");
        return HttpResponse.ok(List.of(new Assignment()));
    }

    @Get("/{id}")
    public HttpResponse<List<Assignment>> getAssignmentByID(Long id) {
        LOGGER.info("AssignmentController.index(Long id) called");
        return HttpResponse.ok(List.of(new Assignment()));
    }

    @Post("/")
    public HttpResponse<Assignment> createAssignment(@Body Assignment assignment) {
        LOGGER.info("AssignmentController.createAssignment(Assignment assignment) called");
        return HttpResponse.created(assignment);
    }

    @Delete("/{id}")
    public HttpResponse<Assignment> deleteAssignment(Long id) {
        LOGGER.info("AssignmentController.deleteAssignment(Long id) called");
        return HttpResponse.noContent();
    }

    @Put("/{id}")
    public HttpResponse<Assignment> updateAssignment(Long id, @Body Assignment assignment) {
        LOGGER.info("AssignmentController.updateAssignment(Long id, Assignment assignment) called");
        return HttpResponse.ok(assignment);
    }
}
