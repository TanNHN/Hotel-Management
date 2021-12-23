/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import tannhn.room_types.RoomTypeDAO;
import tannhn.room_types.RoomTypeDTO;

/**
 *
 * @author PC
 */
public class GetRoomFromHotelControlller extends HttpServlet {

    private static Logger LOGGER = Logger.getLogger(GetRoomFromHotelControlller.class);
    private static String GET_ROOM_PAGE = "hotel_room.jsp";
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
        String url = GET_ROOM_PAGE;
        RoomTypeDAO roomTypeDAO = new RoomTypeDAO();
        String checkIn = "";
        String checkOut = "";
        String hotelName = request.getParameter("hotelName");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedCheckInDate = new Date();
        Date parsedCheckOutDate = new Date();
        try {

            if (request.getParameter("txtCheckIn") != null) {
                if (request.getParameter("txtCheckIn").length() > 0) {
                    checkIn = request.getParameter("txtCheckIn");
                    parsedCheckInDate = dateFormat.parse(checkIn);

                }
            }
            if (request.getParameter("txtCheckOut") != null) {
                if (request.getParameter("txtCheckOut").length() > 0) {
                    checkOut = request.getParameter("txtCheckOut");
                    parsedCheckOutDate = dateFormat.parse(checkOut);

                }
            }
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            List<RoomTypeDTO> roomTypeList = roomTypeDAO.getAllRoomFromRoomType(parsedCheckInDate, parsedCheckOutDate, hotelId);
            request.setAttribute("ROOM_TYPE_LIST", roomTypeList);
            request.setAttribute("HOTEL_ID", hotelId);
            request.setAttribute("CHECK_IN", dateFormat.format(parsedCheckInDate));
            request.setAttribute("CHECK_OUT", dateFormat.format(parsedCheckOutDate));
        } catch (Exception e) {
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
