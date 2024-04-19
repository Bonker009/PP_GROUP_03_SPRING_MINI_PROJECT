package org.example.miniprojectspring.service;

import org.example.miniprojectspring.model.entity.Expense;
import org.example.miniprojectspring.model.request.ExpenseRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    List<Expense> getAllExpenses(UUID userId,Integer page, Integer size);

    Expense findExpenseById(UUID id);

    Expense postExpense(ExpenseRequest request,UUID userId);

    Expense updateExpenseById(UUID id,ExpenseRequest request);


    Expense deleteExpenseById(UUID id);
}
