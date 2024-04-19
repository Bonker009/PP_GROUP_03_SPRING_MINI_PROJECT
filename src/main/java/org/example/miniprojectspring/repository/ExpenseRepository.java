package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.example.miniprojectspring.configuration.UUIDTypeHandler;
import org.example.miniprojectspring.model.entity.Expense;
import org.example.miniprojectspring.model.request.ExpenseRequest;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ExpenseRepository {
    @Results(id = "expenseMapping", value = {
            @Result(property = "expenseId" , column = "expense_id",jdbcType = JdbcType.OTHER,javaType = UUID.class, typeHandler = UUIDTypeHandler.class),
            @Result(property = "appUserDTO",column = "user_id", one = @One(select = "org.example.miniprojectspring.repository.AppUserRepository.findUserById")),
            @Result(property = "category",column = "category_id", one = @One(select = "org.example.miniprojectspring.repository.CategoryRepository.getCategoryByCategoryId"))
    })
    @Select("""
            SELECT * FROM expenses WHERE user_id = #{userId}
            """)
    List<Expense> getAllExpenses(UUID userId,Integer page, Integer size);


    @Select("""
    SELECT * FROM expenses WHERE expense_id = #{expenseId}::uuid;
    """)
    @ResultMap("expenseMapping")
    Expense getExpenseById (@Param("expenseId") UUID expenseId);

    @Select("""
    INSERT INTO expenses (amount, description, date, user_id, category_id) 
     VALUES (#{expenses.amount}, #{expenses.description}, #{expenses.date}, #{userId}, #{expenses.categoryId})
     RETURNING *
    """)
    @ResultMap("expenseMapping")
    Expense createExpense(@Param("expenses") ExpenseRequest request,UUID userId);

    Expense updateExpenseById(UUID id, ExpenseRequest request);

    @Select("""
    DELETE FROM expenses WHERE expense_id = #{expenseId}::uuid 
    RETURNING *
    """)
    @ResultMap("expenseMapping")
    Expense deleteExpenseById( @Param("expenseId") UUID expenseId);
}
