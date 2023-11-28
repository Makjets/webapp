package tech.stark.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import tech.stark.webapp.controller.error.BadRequestException;
import tech.stark.webapp.controller.error.ServiceUnavailableException;
import tech.stark.webapp.models.Assignment;
import tech.stark.webapp.models.Submission;
import tech.stark.webapp.models.TopicMessage;
import tech.stark.webapp.repository.AssignmentRepository;
import tech.stark.webapp.repository.SubmissionRepository;
import tech.stark.webapp.security.SecurityService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentService {

    private final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);

    @Value("${aws.sns.topicArn}")
    private String topic_arn;

    private final SecurityService securityService;


    private AccountService accountService;

    private final AssignmentRepository assignmentRepository;

    private final SubmissionRepository submissionRepository;

    private SnsClient snsClient;

    @Autowired
    public AssignmentService(SecurityService securityService, AccountService accountService, AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository, SnsClient snsClient) {
        this.securityService = securityService;
        this.accountService = accountService;
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.snsClient = snsClient;
    }

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

    public Optional<Submission> postSubmission(Submission submission, String id) {
        if(!assignmentRepository.existsById(id)){
            throw new BadRequestException("assignment id "+id+" doesn't exist");
        }
        Assignment assignment = assignmentRepository.findById(id).get();
        try {
            submission.setAssignment_id(id);
            String now = Instant.now().toString();
            submission.setSubmission_date(now);
            submission.setAssignment_updated(now);
            submission.setUser_email(securityService.getUser().getUsername());
            submissionRepository.save(submission);

        } catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ServiceUnavailableException("");
        }
        TopicMessage message = new TopicMessage();
        message.setSubmission(submission);
        message.setAccount(accountService.getByEmail(submission.getUser_email()).get());
        message.setAssignment(assignment);
        LOGGER.info("Posting message: "+message.toString());
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message.toString())
                    .topicArn(topic_arn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            LOGGER.info(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
            LOGGER.error(e.awsErrorDetails().errorMessage());
        }
        return Optional.of(submission);
    }

}
