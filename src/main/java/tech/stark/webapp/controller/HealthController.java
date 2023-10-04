package tech.stark.webapp.controller;

import java.util.concurrent.atomic.AtomicLong;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthz")
public class HealthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> greeting(HttpServletRequest request, @RequestBody @Nullable String body) {
        if(request.getQueryString() != null || body != null){
            return ResponseEntity.badRequest().cacheControl(CacheControl.noCache()).build();
        }

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).build();
    }
}