package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.example.miniprojectspring.configuration.UUIDTypeHandler;
import org.example.miniprojectspring.model.dto.OptsDTO;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface OneTimePasswordRepository {



    Optional<OptsDTO> findById(Integer id);


    @Select("""
            INSERT INTO otps (opt_code, expiration,verified,user_id) VALUES(#{opt.optCode},#{opt.expiration},false,#{opt.userId})
            """)

    void createNewOpt(@Param("opt") OptsDTO optsDTO);

    @Select("""
            SELECT * FROM otps WHERE opt_code = #{code}
            """)

    OptsDTO findByCode(String code);

    @Update("""
            UPDATE otps SET verified = true WHERE opt_code = #{code}
            """)

    void save(String code);

    @Update("""
            UPDATE otps SET verified = true WHERE opt_code = #{optCode}
            """)
    void verify(String optCode);

    @Select("""
            SELECT * FROM otps WHERE user_id = #{userId} ORDER BY issued_at LIMIT 1
            """)
    @Results(id = "otp", value = {
            @Result(column = "user_id", property = "userId", javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),
            @Result(property = "issuedDate", column = "issued_at"),
            @Result(property = "optCode", column = "opt_code"),
            @Result(property = "verify",column = "verified")

    })
    OptsDTO findOptByUserId(UUID userId);
}
