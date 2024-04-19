package org.example.miniprojectspring.repository;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.example.miniprojectspring.configuration.UUIDTypeHandler;
import org.example.miniprojectspring.model.entity.Expense;
import org.example.miniprojectspring.model.request.ExpenseRequest;

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

//    @Select("""
//    SELECT * FROM expenses WHERE expense_id= #{expenseId}::uuid;
//    """)
//    @ResultMap("expensesMapping")
//    Expense findExpenseById(@Param("expenseId") UUID expenseId);

    @Select("""
    SELECT * FROM expenses WHERE expense_id = #{expenseId}::uuid;
    """)
    @ResultMap("expenseMapping")
    Expense getExpenseById(@Param("expenseId") UUID expenseId);

    Expense createExpense(ExpenseRequest request);

    Expense updateExpenseById(UUID id, ExpenseRequest request);

    Expense deleteExpenseById(UUID id);
}
