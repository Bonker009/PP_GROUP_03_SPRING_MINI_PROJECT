package org.example.miniprojectspring.service.ServiceImplementation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
        return expenseRepository.getAllExpenses(userId,page,size);
    }

    @Override
    public Expense findExpenseById(UUID id) {
        return expenseRepository.getExpenseById(id);
    }

    @Override
    public Expense postExpense(ExpenseRequest request) {
        return expenseRepository.createExpense(request);
    }

    @Override
    public Expense updateExpenseById(UUID id, ExpenseRequest request) {
        return expenseRepository.updateExpenseById(id,request);
    }

    @Override
    public Expense deleteExpenseById(UUID id) {
        return expenseRepository.deleteExpenseById(id);
    }
}
