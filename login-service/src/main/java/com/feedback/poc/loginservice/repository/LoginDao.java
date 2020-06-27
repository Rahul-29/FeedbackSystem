package com.feedback.poc.loginservice.repository;

import com.feedback.poc.loginservice.domain.Employee;
import com.feedback.poc.loginservice.domain.LoginResponse;
import com.feedback.poc.loginservice.domain.UpdateResponse;
import com.feedback.poc.loginservice.domain.UserCredential;
import com.feedback.poc.loginservice.domain.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {

    private final String VALID_EMPLOYEE = "select count(*) from feedback.employee where employee_id = ?";
    private final String CREATE_LOGIN = "insert into feedback.login (user_id,password) values(?,?)";
    private final String CREATE_EMPLOYEE = "insert into feedback.employee (employee_id, first_name, last_name, designation, manager_id) values(?,?,?,?,?)";
    private final String VALIDATE_LOGIN = "select password from feedback.login where user_id=?";
    private final String DELETE_LOGIN = "delete from feedback.login where user_id=?";
    private final String DELETE_RATING = "delete from feedback.rating where employee_id=?";
    private final String DELETE_EMPLOYEE = "delete from feedback.employee where employee_id=?";
    private final String UPDATE_LOGIN = "update feedback.login set password =? where user_id=?";
    private final String UPDATE_EMPLOYEE = "update feedback.employee set first_name=?,last_name=?,designation=?, manager_id=? where employee_id=?";
    private final String FETCH_EMPLOYEE = "select employee_id, first_name, last_name, designation, manager_id from feedback.employee where employee_id=?";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public LoginDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * @param employee
     * @return
     */
    public RegisterResponse register(Employee employee)
    {
        RegisterResponse response = new RegisterResponse();
        Integer count = jdbcTemplate.queryForObject(VALID_EMPLOYEE, new Object[]{employee.getManagerId()}, Integer.class);
        if(count == 0){
            response.setResponse("Manager id is invalid");
            response.setStatus(-1);
            return response;
        }
        int loginTableStatus = jdbcTemplate.update(CREATE_LOGIN, employee.getEmployeeId(), passwordEncoder().encode(employee.getPassword()));

        int employeeTableStatus = jdbcTemplate.
                update(CREATE_EMPLOYEE, employee.getEmployeeId(), employee.getFirstName(),
                        employee.getLastName(), employee.getDesignation(), employee.getManagerId());

        int result = loginTableStatus & employeeTableStatus;
        response.setResponse(result == 1 ? "Success" : "Failure");
        response.setStatus(result);
        return response;
    }

    /**
     *
     * @param userCredential
     * @return
     */
    public Boolean login(UserCredential userCredential) {
        String password = jdbcTemplate.queryForObject(VALIDATE_LOGIN, new Object[]{userCredential.getUsername()}, String.class);
        return passwordEncoder().matches(userCredential.getPassword(), password);
    }

    public String removeUser(UserCredential userCredential) {
        int loginRemoveStatus = jdbcTemplate.update(DELETE_LOGIN, userCredential.getUsername());
        int ratingRemoveStatus = jdbcTemplate.update(DELETE_RATING, userCredential.getUsername());
        int employeeRemoveStatus = jdbcTemplate.update(DELETE_EMPLOYEE, userCredential.getUsername());

        return (loginRemoveStatus & ratingRemoveStatus & employeeRemoveStatus) == 1 ?
                "User removed successfully" : "Error during removing user";
    }

    /**
     *
     * @param employee
     * @return
     */
    public UpdateResponse updateUser(Employee employee) {
        UpdateResponse response = new UpdateResponse();
        int loginStatus = jdbcTemplate.update(UPDATE_LOGIN, passwordEncoder().encode(employee.getPassword()), employee.getEmployeeId());
        int employeeStatus = jdbcTemplate.update(UPDATE_EMPLOYEE, employee.getFirstName(), employee.getLastName(),
                employee.getDesignation(), employee.getManagerId(), employee.getEmployeeId());

        int result = loginStatus & employeeStatus&employeeStatus;

        if (result == 1) {
            response.setEmployee(employee);
            response.setUpdateStatus("Updated");
        } else {
            response.setUpdateStatus("No user with mentioned user_id "+ employee.getEmployeeId());
        }
        return response;
    }
}
