package com.murali.expensetracker.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UrlCreation {
    public String createApplicationUrl(HttpServletRequest request) {
        return "http://"
                +request.getServerName()+":"
                +request.getServerPort()
                +request.getContextPath();
    }
}
