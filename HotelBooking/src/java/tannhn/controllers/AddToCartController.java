/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import tannhn.cart_item.CartItem;
import tannhn.hotels.HotelDAO;
import tannhn.hotels.HotelDTO;
import tannhn.room_type_price.RoomTypePriceDTO;
import tannhn.room_types.RoomTypeDAO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDAO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class AddToCartController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddToCartController.class);
    private static final String INVALID = "invalid.html";
    private static final String SEARCH_ROOM_PAGE = "GetRoomFromHotelControlller";

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
        String url = SEARCH_ROOM_PAGE;
        HttpSession session = request.getSession();
        RoomDAO roomDAO = new RoomDAO();
        PrintWriter out = response.getWriter();
        HotelDAO hotelDAO = new HotelDAO();
        RoomTypeDAO roomTypeDAO = new RoomTypeDAO();

        try {
            boolean isAllEmpty = true;
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            String checkIn = request.getParameter("txtCheckIn");
            String checkOut = request.getParameter("txtCheckOut");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date checkInDate = new Date();
            Date checkOutDate = new Date();

            if (checkIn != null) {
                if (checkIn.length() > 0) {
                    checkInDate = dateFormat.parse(checkIn);
                }
            }
            if (checkOut != null) {
                if (checkOut.length() > 0) {
                    checkOutDate = dateFormat.parse(checkOut);
                }
            }
            List<RoomTypeDTO> roomTypeList = roomTypeDAO.getAllRoomFromRoomType(checkInDate, checkOutDate, hotelId);

            List<CartItem> cart = new ArrayList<>();
            HotelDTO hotel = hotelDAO.getHotelDetails(hotelId);
            if (session.getAttribute("CART") != null) {
                cart = (List<CartItem>) session.getAttribute("CART");

                for (RoomTypeDTO roomType : roomTypeList) {
                    int quantity = Integer.parseInt(request.getParameter("txtQuantity" + roomType.getId()));
                    if (quantity > 0) {
                        isAllEmpty = false;
                        Iterator<CartItem> i = cart.iterator();
                        while (i.hasNext()) {
                            CartItem cartItem = i.next();
                            if (cartItem.getHotel().getId() == hotelId && cartItem.getRoomType().getId() == roomType.getId()) {
                                i.remove();
                            }
                        }
                        List<RoomDTO> roomDetailsList = roomDAO.searchRoomForBooking(checkInDate, checkOutDate, roomType.getId(), hotelId, quantity);
                        int dayRent = (int) ((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));

                        cart.add(
                                new CartItem(hotel, new RoomTypeDTO(
                                        roomType.getId(),
                                        roomType.getName(),
                                        roomType.getTotalRoom(),
                                        new RoomTypePriceDTO(roomType.getRoomTypePrice().getPrice()),
                                        roomDetailsList),
                                        checkInDate,
                                        checkOutDate,
                                        quantity,
                                        quantity * roomType.getRoomTypePrice().getPrice() * (dayRent + 1),
                                        roomType.getTotalRoom()
                                )
                        );
                    }

                }
            } else {

                for (RoomTypeDTO roomType : roomTypeList) {
                    int quantity = Integer.parseInt(request.getParameter("txtQuantity" + roomType.getId()));
                    if (quantity > 0) {
                        isAllEmpty = false;

                        List<RoomDTO> roomDetailsList = roomDAO.searchRoomForBooking(checkInDate, checkOutDate, roomType.getId(), hotelId, quantity);
                        int dayRent = (int) ((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));

                        cart.add(
                                new CartItem(hotel, new RoomTypeDTO(
                                        roomType.getId(),
                                        roomType.getName(),
                                        roomType.getTotalRoom(),
                                        new RoomTypePriceDTO(roomType.getRoomTypePrice().getPrice()),
                                        roomDetailsList),
                                        checkInDate,
                                        checkOutDate,
                                        quantity,
                                        quantity * roomType.getRoomTypePrice().getPrice() * (dayRent + 1),
                                        roomType.getTotalRoom())
                        );
                    }

                }

            }
            if (isAllEmpty) {
                request.setAttribute("ADD_TO_CART_SUCCESS_MESSAGE", "No room was selected");
            } else {
                request.setAttribute("ADD_TO_CART_SUCCESS_MESSAGE", "Added to cart!");

            }
            session.setAttribute("CART", cart);
            request.setAttribute("HOTEL_ID", hotelId);
            request.setAttribute("CHECK_IN", checkInDate);
            request.setAttribute("CHECK_OUT", checkOutDate);

        } catch (Exception e) {
            LOGGER.error(e);
            url = INVALID;
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
