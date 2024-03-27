package com.murali.expensetracker.controller;

import com.murali.expensetracker.model.ExpenseModel;
import com.murali.expensetracker.response.ResponseStatus;
import com.murali.expensetracker.service.expense.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseController {
    private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }
    @PostMapping("/expense")
    public ResponseEntity<ResponseStatus>addExpense(@RequestBody ExpenseModel expenseModel){
        expenseService.saveExpense(expenseModel);
        ResponseStatus responseStatus = new ResponseStatus("expense saved successfully", HttpStatus.OK);
        return new ResponseEntity<>(responseStatus,HttpStatus.OK);
    }
}
