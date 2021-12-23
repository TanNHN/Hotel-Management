/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import tannhn.accounts.AccountDAO;
import tannhn.accounts.AccountDTO;
import tannhn.helpers.Helper;

/**
 *
 * @author PC
 */
public class RegisterController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterController.class);

    private static final String REGISTER_PAGE = "register.jsp";
    private static final String LOGIN = "login.html";
    private static final String INVALID = "invalid.html";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        AccountDAO accountDAO = new AccountDAO();
        Helper helper = new Helper();
        String url = LOGIN;
        try {
            String userId = request.getParameter("txtUserId");
            if (accountDAO.checkDuplicateUserId(userId)) {
                url = REGISTER_PAGE;
                request.setAttribute("ID_EXIST_MESSAGE", "Your user id is already exist!");
            } else {
                String name = request.getParameter("txtName");
                String address = request.getParameter("taAddress");
                String phone = request.getParameter("txtPhone");
                String password = request.getParameter("txtPassword");
                Date date = new Date();
                String passwordHash = helper.hashPassword(password);
                AccountDTO accountDTO = new AccountDTO(userId, name, passwordHash, phone, address, 2, date, 1);
                if (accountDAO.register(accountDTO)) {
                    LOGGER.info("User " + userId + " has registered at " + date);
                } else {
                    LOGGER.info("User " + userId + " has faileld when registered at " + date);
                    url = INVALID;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
            url = INVALID;
        } finally {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
