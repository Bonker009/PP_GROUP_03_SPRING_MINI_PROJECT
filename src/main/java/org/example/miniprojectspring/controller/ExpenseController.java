package org.example.miniprojectspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.miniprojectspring.model.entity.CustomUserDetail;
import org.example.miniprojectspring.model.entity.Expense;
import org.example.miniprojectspring.model.request.ExpenseRequest;
import org.example.miniprojectspring.model.response.ApiResponse;
import org.example.miniprojectspring.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/expense")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Get All Expenses")
    public ResponseEntity<?> getAllExpense(@RequestParam(defaultValue = "5") @Positive(message = "size cannot be negative or 0") Integer size,
                                           @RequestParam(defaultValue = "1") @Positive(message = "page cannot be negative or 0") Integer page,
                                           @RequestParam(defaultValue = "expense_id") @NotBlank(message = "order by cannot be blank") String orderBy,
                                           @RequestParam(defaultValue = "false") Boolean ascOrDesc
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        List<Expense> expenseList = expenseService.getAllExpenses(userId,page,size);
        System.out.println(expenseList);
        ApiResponse<List<Expense>> apiResponse = ApiResponse.<List<Expense>>builder()
                .status(HttpStatus.OK)
                .payload(expenseList)
                .message("Get all expenses successfully")
                .code(201)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Expense by Id")
    public ResponseEntity<?> getExpenseById(@PathVariable UUID id) {
        Expense expense = expenseService.findExpenseById(id);
        ApiResponse<Expense> apiResponse = ApiResponse.<Expense>builder()
                .payload(expense)
                .message("Get Expense with Id : " + id + " successful")
                .code(201)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    @Operation(summary = "Create new Expense")
    public ResponseEntity<?> createExpense(@Valid @RequestBody ExpenseRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        Expense expense = expenseService.postExpense(request,userId);
        ApiResponse<Expense> apiResponse = ApiResponse.<Expense>builder()
                .payload(expense)
                .message("Created new Expense successfully")
                .code(201)
                .status(HttpStatus.CREATED)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Expense by Id")
    public ResponseEntity<?> updateExpenseById(@PathVariable UUID id,@Valid  @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.updateExpenseById(id, request);
        ApiResponse<Expense> apiResponse = ApiResponse.<Expense>builder()
                .payload(expense)
                .message("Get Expense with Id : " + id + " successful")
                .code(201)
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Expense By Id")
    public ResponseEntity<?> deleteExpenseById(@PathVariable UUID id) {
         expenseService.deleteExpenseById(id);
        ApiResponse<Expense> apiResponse = ApiResponse.<Expense>builder()
                .payload(null)
                .message(" Expense with Id : " + id + "has been removed successful.")
                .code(201)
                .status(HttpStatus.OK)
                .localDateTime(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
