package com.feedback.poc.service;

import com.feedback.poc.domain.Rating;
import com.feedback.poc.domain.Response;
import com.feedback.poc.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private RatingRepository repository;

    public boolean saveRating(final Rating rating){
        return repository.insertRating(rating);
    }


    /**
     *
     * @param id
     * @return
     */
    public Response retrieveRating(final String id){
        Response response = new Response();
        List<Rating> ratingAsEmployee =  repository.retrieveRatingAsEmployee(id);
        List<Rating> ratingAsManager = repository.retrieveRatingAsManager(id);

        if(null != ratingAsEmployee && ratingAsEmployee.size() > 0 && (ratingAsManager.size() == 0)){
            response = createResponse(ratingAsEmployee.get(0), null);
        }
        else if(ratingAsEmployee.size() > 0 && ratingAsManager.size() > 0){
            Double ratingFromSubordinate = 0.0;
            Double ratingFromManager = 0.0;
            Double averageRating = 0.0;
            for(Rating rating : ratingAsManager){
                Double rating1 = .3 * Integer.parseInt(rating.getEmployee_rating());
                ratingFromSubordinate += rating1;
            }
            Rating rating = ratingAsEmployee.get(0);
            ratingFromManager = .7 * Integer.parseInt(rating.getManager_rating());
            averageRating = ratingFromSubordinate + ratingFromManager;

            response = createResponse(rating, averageRating);
        }

        return response;
    }

    /**
     *
     * @param rating
     * @return
     */
    private Response createResponse(final Rating rating, final Double ratingReceived){
        Response response = new Response();
        if(null != ratingReceived) {
            if(ratingReceived > 4)
                response.setRating("4");
            else{
                response.setRating(String.valueOf(Math.round(ratingReceived.doubleValue())));
            }
        }
        else
            response.setMessage("No rating available");

        return response;
    }
}