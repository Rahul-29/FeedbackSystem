package com.feedback.poc.repository;

import com.feedback.poc.domain.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RatingRepository {

    final String CREATE_RATING = "INSERT INTO feedback.Rating (employee_id, manager_id, employee_rating, manager_rating) values (?,?,?,?)";
    final String GET_RATING_AS_EMPLOYEE = "select employee_id, manager_id, manager_rating from feedback.rating where employee_id=?";
    final String GET_RATING_AS_MANAGER = "select employee_id, manager_id, employee_rating from feedback.rating where manager_id=?";
    final String RATING_COUNT = "select count(*) from feedback.rating where employee_id=?";
    final String UPDATE_MANAGER_RATING = "update rating set manager_rating = ? where employee_id=?";
    final String UPDATE_EMPLOYEE_RATING = "update rating set employee_rating = ? where employee_id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param id
     * @return
     */
    public List<Rating> retrieveRatingAsEmployee(final String id){
        List<Rating> ratingList = jdbcTemplate.query(GET_RATING_AS_EMPLOYEE, new Object[]{id}, new BeanPropertyRowMapper<Rating>(Rating.class));
        return ratingList;
    }

    /**
     *
     * @param id
     * @return
     */
    public List<Rating> retrieveRatingAsManager(final String id){
        List<Rating> ratingList = jdbcTemplate.query(GET_RATING_AS_MANAGER, new Object[]{id}, new BeanPropertyRowMapper<Rating>(Rating.class));
        return ratingList;
    }

    /**
     *
     * @param rating
     */
    public boolean insertRating(Rating rating){
        Integer employeeCount = jdbcTemplate.queryForObject(RATING_COUNT, new Object[]{rating.getEmployee_id()}, Integer.class);
        if(employeeCount != 0){
            if(null != rating.getManager_rating()){
                jdbcTemplate.update(UPDATE_MANAGER_RATING, rating.getManager_rating(), rating.getEmployee_id());
            }
            else{
                jdbcTemplate.update(UPDATE_EMPLOYEE_RATING, rating.getEmployee_rating(), rating.getEmployee_id());
            }
        }
        else {
            jdbcTemplate.update(CREATE_RATING, rating.getEmployee_id(), rating.getManager_id(), rating.getEmployee_rating(), rating.getManager_rating());
        }
        return true;
    }
}
