/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import tannhn.bookings.BookingDAO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class ConfirmBookingController extends HttpServlet {

    private static Logger LOGGER = Logger.getLogger(ConfirmBookingController.class);
    private static final String SEARCH_BOOKING = "SearchBookingController";
    private static String INVALID = "invalid.html";

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
        String url = SEARCH_BOOKING;
        BookingDAO bookingDAO = new BookingDAO();
        List<RoomDTO> bookedRoomList = new ArrayList<>();
        List<String> invalidMessages = new ArrayList<>();

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String activateCode = request.getParameter("activateCode");
            bookedRoomList = bookingDAO.checkBookingStatus(bookingId);
            if (bookedRoomList.size() > 0) {
                for (RoomDTO roomDTO : bookedRoomList) {
                    invalidMessages.add("Room " + roomDTO.getName() + " has been booked.");
                }
                request.setAttribute("INVALID_MESSAGES", invalidMessages);
                bookingDAO.deactiveBooking(bookingId);
            } else {
                boolean check = bookingDAO.activeBooking(bookingId, activateCode);

                if (!check) {
                    url = INVALID;
                } else {
                    request.setAttribute("CONFIRM_SUCCESS_MESSAGE", "Booking id: " + bookingId + " has been confirmed");
                    LOGGER.info("booking id: " + bookingId + " has been confirmed");
                }
            }

        } catch (Exception e) {
            url = INVALID;
            LOGGER.error(e);
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
