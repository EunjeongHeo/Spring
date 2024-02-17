package com.group.libraryapp.controller.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //jdbcTemplate 을 선언하고, 컨트롤러의 생성자를 만들어 jdbcTemplate 을 파라미터로 넣으면, 자동으로 들어온다


    @PostMapping("/user") // POST /user
    public void saveUser(@RequestBody UserCreateRequest request) {
        String sql = "INSERT INTO user(name, age) VALUES (?,?)";
        //sql 문을 문자열 변수로 만들어 저장
        //값이 들어가는 부분에 ?를 넣으면 값을 유동적으로 넣을 수 있다

        jdbcTemplate.update(sql, request.getName(), request.getAge());
        //첫 파라미터로는 sql 을 받고, ?를 대신할 값을 차례로 넣는다. 이로써 jdbc 템플릿을 통해 데이터베이스에 적용이 가능하다.
        //여기서의 .update 는 변경을 의미하므로, 넘겨줄 sql 문으로써 INSERT, UPDATE, DELETE 모두 사용 가능하다.
    }


    @GetMapping("/user")
    public List<UserResponse> getUsers() {
        String sql = "SELECT * FROM user";

        // [람다식 부분 역할] : SQL 쿼리 실행 결과의 각 행을 자바 객체로 매핑하는 역할
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id"); // ResultSet rs 에서 "id" 컬럼의 값을 long 타입으로 추출
            String name = rs.getString("name"); // ResultSet rs 에서 "name" 컬럼의 값을 String 타입으로 추출
            int age = rs.getInt("age"); // ResultSet rs 에서 "age" 컬럼의 값을 int 타입으로 추출
            return new UserResponse(id, name, age); //추출한 값을 사용하여 UserResponse 객체를 생성하고 반환한다.
        });

        /*람다식 사용 전 원래 코드 (익명클래스는 사용했음)
        return jdbcTemplate.query(sql, new RowMapper<UserResponse>() {
            @Override
            public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                return new UserResponse(id, name, age);
            }
        });
        */
    }

    @PutMapping("/user")
    public void UpdateUser(@RequestBody UserUpdateRequest request){
        String sql = "UPDATE user SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, request.getName(), request.getId());
    }

}
































