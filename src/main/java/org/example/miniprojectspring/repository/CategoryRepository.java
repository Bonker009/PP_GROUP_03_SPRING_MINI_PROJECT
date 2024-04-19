package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;

import org.apache.ibatis.type.JdbcType;
import org.example.miniprojectspring.configuration.UUIDTypeHandler;
import org.example.miniprojectspring.model.entity.Category;
import org.example.miniprojectspring.model.request.CategoryRequest;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CategoryRepository {
    @Results(id = "category", value = {
            @Result(property = "categoryId", column = "category_id",javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class),

            @Result(property = "userDTO", column = "user_id",javaType = UUID.class, jdbcType = JdbcType.OTHER, typeHandler = UUIDTypeHandler.class,
                    one = @One(select = "org.example.miniprojectspring.repository.AppUserRepository.findUserById")
            )
    })

    @Select("""
                SELECT * FROM categories WHERE category_id = #{categoryId} 
            """)
    Category getCategoryById(UUID userId,@Param("categoryId") UUID categoryId);

    @Select("""
                DELETE FROM categories WHERE category_id = #{categoryId} RETURNING *
            """)
    @ResultMap("category")
    Category deleteCategoryById(@Param("categoryId") UUID categoryId);

    @Select("""
             UPDATE categories SET name = #{request.name} , description = #{request.description}
             WHERE category_id = #{categoryId} AND user_id = #{userId} RETURNING *
            """)
            @Result(property = "categoryId", column = "category_id")
//    @ResultMap("category")
    Category updateCategoryById(UUID userId,UUID categoryId, @Param("request") CategoryRequest request);

    @Select("""
            INSERT INTO categories (name, description, user_id) VALUES (#{request.name},#{request.description},#{userId}) RETURNING *
            """)
    @ResultMap("category")
    Category createNewCategory(@Param("request") CategoryRequest request, @Param("userId") UUID userId);

    @Select("""
            SELECT * FROM categories WHERE user_id = #{userID} LIMIT #{size} OFFSET (#{page} - 1) * #{page}
            """)
    @ResultMap("category")
    List<Category> getAllCategories(UUID userID, Integer size, Integer page);


    @Select("""
    SELECT * FROM categories WHERE category_id = #{categoryId}
""")
    @ResultMap("category")
    Category getCategoryByCategoryId(UUID categoryId);
}
