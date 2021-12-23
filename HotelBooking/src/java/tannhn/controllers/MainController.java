/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author PC
 */
public class MainController extends HttpServlet {

    private static final String LOGIN = "LoginController";
    private static final String SEARCH_HOTEL = "SearchHotelController";
    private static final String SEARCH_ROOM = "SearchRoomController";
    private static final String ADD_TO_CART = "AddToCartController";
    private static final String UPDATE_CART = "UpdateCartController";
    private static final String REGISTER = "RegisterController";
    private static final String DELETE_CART_ITEM = "DeleteCartItemController";
    private static final String CHECK_OUT = "CheckOutController";
    private static final String CONFIRM_BOOKING = "ConfirmBookingController";
    private static final String INVALID = "invalid.html";
    private static final String SEARCH_BOOKING = "SearchBookingController";
    private static final String DEACTIVE_BOOKING = "DeactiveBookingControlller";
    private static final String GET_ROOM = "GetRoomFromHotelControlller";
    private static final String LOG_OUT = "LogOutController";

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
        PrintWriter out = response.getWriter();

        String url = INVALID;
        try {
            String action = request.getParameter("action");
            if (action == null) {
                url = SEARCH_HOTEL;
            } else if (action.equals("Login")) {
                url = LOGIN;
            } else if (action.equals("Register")) {
                url = REGISTER;
            } else if (action.equals("Search Hotel")) {
                url = SEARCH_HOTEL;
            } else if (action.equals("Search room")) {
                url = SEARCH_ROOM;
            } else if (action.equals("Add to cart")) {
                url = ADD_TO_CART;
            } else if (action.equals("Update cart")) {
                url = UPDATE_CART;
            } else if (action.equals("Delete cart item")) {
                url = DELETE_CART_ITEM;
            } else if (action.equals("Check out")) {
                url = CHECK_OUT;
            } else if (action.equals("Search booking")) {
                url = SEARCH_BOOKING;
            } else if (action.equals("Deactive booking")) {
                url = DEACTIVE_BOOKING;
            } else if (action.equals("ConfirmBooking")) {
                url = CONFIRM_BOOKING;
            } else if (action.equals("GetRoom")) {
                url = GET_ROOM;
            }
            else if (action.equals("Log out")) {
                url = LOG_OUT;
            }
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
            out.close();
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
