package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import org.example.miniprojectspring.configuration.UUIDTypeHandler;
import org.example.miniprojectspring.model.entity.AppUser;
import org.example.miniprojectspring.model.dto.AppUserDTO;
import org.example.miniprojectspring.model.request.AppUserRequest;
import org.example.miniprojectspring.model.request.PasswordRequest;

import java.util.UUID;

@Mapper
public interface AppUserRepository {
    @Results(id = "userMapping", value = {
            @Result(column = "user_id", property = "userId", javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "profileImage", column = "profile_image")
    })
    @Select("""
            INSERT INTO users VALUES (DEFAULT,DEFAULT,#{user.email} , #{user.password},#{user.profileImage}) RETURNING *
            """)
    AppUserDTO saveUser(@Param("user") AppUserRequest appUserRequest);

    @Select("""
            SELECT * FROM users WHERE email = #{email}
            """)
    @ResultMap("userMapping")
    AppUserDTO findByEmail(@Param("email") String email);


    @Select("""
            SELECT * FROM users WHERE user_id = #{userId}::uuid
            """)
    @ResultMap("userMapping")
    AppUserDTO findUserById(UUID userId);


    @Select("""
                    UPDATE users SET password = #{request.password} WHERE email = #{email}
            """)
    void resetPasswordById(@Param("request") PasswordRequest passwordRequest, String email);
}
