package com.feedback.poc.controller;

import com.feedback.poc.domain.Rating;
import com.feedback.poc.domain.Response;
import com.feedback.poc.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController("feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<Response> postFeedback(@RequestParam(value = "rating") String rate, @RequestBody Rating rating,
                                               HttpServletRequest request) throws URISyntaxException
    {
        String username = validUser(request);
        if(!(rating.getEmployee_id().equals(rating.getManager_id())) && null != username)
        {
            if (username.equals(rating.getEmployee_id())) {
                rating.setEmployee_rating(rate);
            } else if (username.equals(rating.getManager_id())) {
                rating.setManager_rating(rate);
            } else {
                return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
            }
            boolean result = feedbackService.saveRating(rating);
            if(result){
                Response response = new Response();
                response.setMessage("Rating record successfully");
                return ResponseEntity.created(new URI(rating.getEmployee_id())).body(response);
            }
            return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
        }
        else{
            return createErrorResponse(rating, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public Response getRating(@RequestParam("employeeId") String id){
        return feedbackService.retrieveRating(id);
    }

    public ResponseEntity<Response> createErrorResponse(Rating rating, HttpStatus status){
        Response response = new Response();
        response.setMessage("Invalid Request");
        return ResponseEntity.status(status).body(response);
    }

    /**
     *
     * @param request
     * @return
     */
    private String validUser(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", request.getHeader("Authorization"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8098/token/validate", HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
