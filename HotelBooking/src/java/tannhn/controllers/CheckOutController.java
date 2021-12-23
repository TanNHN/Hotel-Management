/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import tannhn.accounts.AccountDTO;
import tannhn.booking_details.BookingDetailsDTO;
import tannhn.bookings.BookingDAO;
import tannhn.cart_item.CartItem;
import tannhn.helpers.MailTransferHelper;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class CheckOutController extends HttpServlet {

    private static final String SEARCH_BOOKING = "SearchBookingController";
    private static final String INVALID = "invalid.html";
    private static final Logger LOGGER = Logger.getLogger(CheckOutController.class);
    private static final String LOGIN = "login.html";
    private static final String CART = "cart.jsp";

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
        HttpSession session = request.getSession();
        List<String> invalidMessages = new ArrayList<>();
        BookingDAO bookingDAO = new BookingDAO();
        List<RoomDTO> bookedRoomList = new ArrayList<>();
        PrintWriter out = response.getWriter();
        List<BookingDetailsDTO> bookingDetailsList = new ArrayList<>();
        try {
            AccountDTO user = (AccountDTO) session.getAttribute("USER");
            if (user == null) {
                url = LOGIN;
            } else {

                if (user.getRole().getName().equals("User")) {
                    List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
                    cart.sort(Comparator.comparing(a -> a.getHotel().getId()));

                    for (CartItem cartItem : cart) {
                        for (RoomDTO room : cartItem.getRoomType().getRoomList()) {
                            int dayRent = (int) ((cartItem.getCheckOut().getTime() - cartItem.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));
                            bookingDetailsList.add(new BookingDetailsDTO(
                                    new RoomDTO(cartItem.getHotel().getId()),
                                    room.getId(), 
                                    cartItem.getCheckIn(), 
                                    cartItem.getCheckOut(), 
                                    cartItem.getRoomType().getRoomTypePrice().getPrice() * (dayRent + 1)));
                        }
                    }

                    bookedRoomList = bookingDAO.checkRoomStatus(bookingDetailsList);
                    if (bookedRoomList.size() > 0) {
                        for (RoomDTO roomDTO : bookedRoomList) {
                            invalidMessages.add("Room " + roomDTO.getName() + " has been booked by someone else.");
                        }
                        request.setAttribute("MESSAGE_LIST", invalidMessages);
                        url = CART;
                    } else {
                        float total = 0;
                        HashMap<Integer, Float> amountMap = new HashMap<Integer, Float>();
                        int hotelId = 0;
                        for (BookingDetailsDTO bookingDetail : bookingDetailsList) {
                            if (hotelId == bookingDetail.getRoom().getHotelId()) {
                                total += bookingDetail.getAmount();
                                amountMap.replace(bookingDetail.getRoom().getHotelId(), total);
                            } else {
                                total = 0;
                                total += bookingDetail.getAmount();
                                amountMap.put(bookingDetail.getRoom().getHotelId(), total);
                                hotelId = bookingDetail.getRoom().getHotelId();
                            }
                        }
                        List<Integer> idList = bookingDAO.insertBooking(bookingDetailsList, user.getId(), amountMap, 3);
                        StringBuffer urlStringBuffer = request.getRequestURL();

                        String urlString = urlStringBuffer.toString();
                        Random random = new Random();
                        String bookingIdString = "";
                        for (Integer bookingId : idList) {

                            int randomCode = random.nextInt(900) + 100;
                            String base = urlString.substring(0, urlString.lastIndexOf('/')) + "/MainController?action=ConfirmBooking&bookingId=" + bookingId + "&activateCode=" + randomCode;
                            MailTransferHelper.sendVerifyCode(user.getId(), base);
                            bookingIdString += " " + bookingId + ", ";
                            bookingDAO.insertCodeToBooking(bookingId, Integer.toString(randomCode));
                        }
                        session.setAttribute("CART", null);
                        request.setAttribute("BOOKING_SUCCESS_MESSAGE", "Booking Id:" + bookingIdString + " waiting for confirm");

                    }

                } else {
                    url = INVALID;
                }

            }
        } catch (Exception e) {
            url = INVALID;
            LOGGER.error(e);
        } finally {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
            requestDispatcher.forward(request, response);
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
