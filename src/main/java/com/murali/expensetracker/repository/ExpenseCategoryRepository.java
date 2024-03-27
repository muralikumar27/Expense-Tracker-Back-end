package com.murali.expensetracker.repository;

import com.murali.expensetracker.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory,Long> {

    @Query("""
            SELECT e FROM ExpenseCategory e WHERE e.CategoryName = :categoryName and (e.user.userId = :userId or e.user.userId is null)
            """)
    ExpenseCategory findExpenseCategoryByCategoryNameAndUserId(String categoryName, long userId);
}
