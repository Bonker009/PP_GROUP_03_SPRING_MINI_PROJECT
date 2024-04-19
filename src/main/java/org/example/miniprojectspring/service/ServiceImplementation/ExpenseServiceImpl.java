package org.example.miniprojectspring.service.ServiceImplementation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.miniprojectspring.exception.SearchNotFoundException;
import org.example.miniprojectspring.model.entity.Expense;
import org.example.miniprojectspring.model.request.ExpenseRequest;
import org.example.miniprojectspring.repository.ExpenseRepository;
import org.example.miniprojectspring.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor

public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    @Override
    public List<Expense> getAllExpenses(UUID userId,Integer page, Integer size) {
        System.out.println(expenseRepository.getAllExpenses(userId,page,size));
        return expenseRepository.getAllExpenses(userId,page,size);
    }

    @Override
    public Expense findExpenseById(UUID id) {
        Expense expense = expenseRepository.getExpenseById(id);
        if (expense == null) {
            throw new SearchNotFoundException("Expense not found with id: " + id + " not found.");
        }
        return expense;
    }

    @Override
    public Expense postExpense(ExpenseRequest request,UUID userId) {
        return expenseRepository.createExpense(request,userId);
    }

    @Override
    public Expense updateExpenseById(UUID id, ExpenseRequest request) {
        Expense expense = expenseRepository.getExpenseById(id);
        if (expense == null) {
            throw new SearchNotFoundException("Expense not found with id: " + id + " not found.");
        }
        return expense;
    }

    @Override
    public Expense deleteExpenseById(UUID id) {
        Expense expense = expenseRepository.deleteExpenseById(id);
        if (expense == null) {
            throw new SearchNotFoundException("Expense not found with id: " + id + " not found.");
        }
        return expense;
    }
}
