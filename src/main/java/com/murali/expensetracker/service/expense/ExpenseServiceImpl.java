package com.murali.expensetracker.service.expense;

import com.murali.expensetracker.entity.Expense;
import com.murali.expensetracker.entity.ExpenseCategory;
import com.murali.expensetracker.entity.User;
import com.murali.expensetracker.model.ExpenseModel;
import com.murali.expensetracker.repository.ExpenseCategoryRepository;
import com.murali.expensetracker.repository.ExpenseRepository;
import com.murali.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    private ExpenseCategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void saveExpense(ExpenseModel expenseModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmailId(username);
        ExpenseCategory expenseCategory = categoryRepository.findExpenseCategoryByCategoryNameAndUserId(expenseModel.getExpenseCategory(), user.getUserId());
        Expense expense = new Expense();
        expense.setAmount(expenseModel.getAmount());
        expense.setDate(expenseModel.getDate());
        expense.setDescription(expenseModel.getDescription());
        expense.setExpenseCategory(expenseCategory);
        expense.setUser(user);

        expenseRepository.save(expense);
    }
}
