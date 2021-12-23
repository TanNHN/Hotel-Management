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
import java.time.Duration;
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
import tannhn.room_type_price.RoomTypePriceDTO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDAO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class UpdateCartController extends HttpServlet {

    private static final String CART_PAGE = "cart.jsp";
    private static final String INAVLID = "invalid.jsp";
    private static final Logger LOGGER = Logger.getLogger(UpdateCartController.class);

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
        String url = CART_PAGE;
        HttpSession session = request.getSession();
        RoomDAO roomDAO = new RoomDAO();
        List<String> messageList = new ArrayList<>();
        try {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
            List<CartItem> newCart = new ArrayList<>();
            Iterator<CartItem> i = cart.iterator();
            while (i.hasNext()) {
                CartItem cartItem = i.next();
                int quantity = Integer.parseInt(request.getParameter("quantityHotelId" + cartItem.getHotel().getId() + "RoomTypeId" + cartItem.getRoomType().getId()));
                if (quantity != cartItem.getQuantity()) {
                    int checkQuantity = roomDAO.searchTotalRsoomForBooking(cartItem.getCheckIn(), cartItem.getCheckOut(), cartItem.getRoomType().getId(), cartItem.getHotel().getId());
                    if (quantity > checkQuantity) {
                        messageList.add("Hotel " + cartItem.getHotel().getName()
                                + " doesn't have enough available room for room type " + cartItem.getRoomType().getName()
                        );
                        newCart.add(cartItem);

                    } else {
                        List<RoomDTO> roomDetailsList = roomDAO.searchRoomForBooking(cartItem.getCheckIn(), cartItem.getCheckOut(), cartItem.getRoomType().getId(), cartItem.getHotel().getId(), quantity);
                        int dayRent = (int) ((cartItem.getCheckOut().getTime() - cartItem.getCheckIn().getTime()) / (1000 * 60 * 60 * 24));

                        newCart.add(
                                new CartItem(cartItem.getHotel(), new RoomTypeDTO(cartItem.getRoomType().getId(),
                                        cartItem.getRoomType().getName(),
                                        cartItem.getRoomType().getTotalRoom(),
                                        new RoomTypePriceDTO(cartItem.getRoomType().getRoomTypePrice().getPrice()),
                                        roomDetailsList),
                                        cartItem.getCheckIn(),
                                        cartItem.getCheckOut(),
                                        quantity,
                                        quantity * cartItem.getRoomType().getRoomTypePrice().getPrice() * (dayRent + 1),
                                        cartItem.getAvailable()
                                )
                        );

                    }
                } else {
                    newCart.add(cartItem);
                }

            }
            request.setAttribute("MESSAGE_LIST", messageList);
            session.setAttribute("CART", newCart);
        } catch (Exception e) {
            LOGGER.error(e);
            url = INAVLID;
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
