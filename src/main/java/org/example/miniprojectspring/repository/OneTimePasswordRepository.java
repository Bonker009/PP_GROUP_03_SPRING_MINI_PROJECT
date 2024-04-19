package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;
import org.example.miniprojectspring.model.dto.OptsDTO;

import java.util.Optional;

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
}
