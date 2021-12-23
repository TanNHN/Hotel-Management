/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import tannhn.accounts.AccountDTO;
import tannhn.bookings.BookingDAO;
import tannhn.bookings.BookingDTO;

/**
 *
 * @author PC
 */
public class SearchBookingController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SearchBookingController.class);
    private static final String SEACH_BOOKING_PAGE = "booking_history.jsp";
    private static final String INVALID = "invalid.html";
    private static final String LOGIN = "login.html";

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
        String url = SEACH_BOOKING_PAGE;
        HttpSession session = request.getSession();
        List<BookingDTO> bookingList;
        String hotelName = "";
        Date createDate = null;
        int skip = 0;
        int total = 0;
        int pageSize = 20;
        int page = 1;
        try {
            AccountDTO user = (AccountDTO) session.getAttribute("USER");
            if (user == null) {
                url = LOGIN;
            } else {
                if (request.getParameter("txtCreateDate") != null) {
                    if (request.getParameter("txtCreateDate").length() > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        createDate = dateFormat.parse(request.getParameter("txtCreateDate"));
                    }
                }
                if (request.getParameter("txtCreateDate") != null) {
                    if (request.getParameter("txtCreateDate").length() > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        createDate = dateFormat.parse(request.getParameter("txtCreateDate"));
                    }
                }
                if (request.getParameter("txtHotelName") != null) {
                    hotelName = request.getParameter("txtHotelName");
                }
                if (request.getParameter("page") == null) {
                    page = 1;
                    skip = 0;
                } else {
                    page = Integer.parseInt(request.getParameter("page"));
                    skip = (page - 1) * pageSize;
                }
                if (request.getParameter("slPageSize") != null) {
                    if (request.getParameter("slPageSize").length() > 0) {
                        pageSize = Integer.parseInt(request.getParameter("slPageSize"));
                    }
                }
                BookingDAO bookingDAO = new BookingDAO();
                bookingList = bookingDAO.searchBooking(user.getId(), hotelName, createDate, skip, pageSize);
                total = bookingDAO.searchTotalBooking(user.getId(), hotelName, createDate);
                request.setAttribute("BOOKING_LIST", bookingList);
                request.setAttribute("PAGE_SIZE", pageSize);
                request.setAttribute("TOTAL", total);
                
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
