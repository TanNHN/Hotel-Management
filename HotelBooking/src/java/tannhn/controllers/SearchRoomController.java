/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import tannhn.room_types.RoomTypeDAO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDAO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class SearchRoomController extends HttpServlet implements Serializable {

    private static Logger LOGGER = Logger.getLogger(SearchRoomController.class);
    private static String SEARCH_ROOM_PAGE = "room_search.jsp";
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
        String url = SEARCH_ROOM_PAGE;
        RoomDAO roomDAO = new RoomDAO();
        RoomTypeDAO roomTypeDAO = new RoomTypeDAO();
        int roomTypeId = 0;
        int skip = 0;
        int total = 0;
        int pageSize = 20;
        int page = 1;
        try {

            List<RoomTypeDTO> roomTypeList = roomTypeDAO.geAllRoomType();
            int hotelId = Integer.parseInt(request.getParameter("hotelId"));
            if (request.getParameter("slRoomTypeId") != null) {
                if (request.getParameter("slRoomTypeId").length() > 0) {
                    roomTypeId = Integer.parseInt(request.getParameter("slRoomTypeId"));

                }
            }

            if (request.getAttribute("HOTEL_ID") != null) {
                hotelId = Integer.parseInt(request.getParameter("hotelId"));
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
            String hotelName = request.getParameter("hotelName");
            List<RoomDTO> roomList = new ArrayList<>();
            String checkIn = null;
            String checkOut = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date parsedCheckInDate = new Date();
            Date parsedCheckOutDate = new Date();

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
            int dayRent = (int) ((parsedCheckOutDate.getTime() - parsedCheckInDate.getTime()) / (1000 * 60 * 60 * 24));
            if (dayRent < 0) {
                request.setAttribute("INVALID_DATE", "Invalid check in/check out date");

            } else {
                Calendar cal = new GregorianCalendar();
                cal.setTime(parsedCheckInDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startCheckinDate = cal.getTime();
                cal = new GregorianCalendar();
                cal.setTime(parsedCheckOutDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
                Date endCheckOutDate = cal.getTime();
//                roomList = roomDAO.searchRoomForBooking(startCheckinDate, endCheckOutDate, roomTypeId, hotelId, skip, pageSize);
//                total = roomDAO.searchTotalRoomForBooking(startCheckinDate, endCheckOutDate, roomTypeId, hotelId);
            }
            request.setAttribute("ROOM_LIST", roomList);
            request.setAttribute("ROOM_TYPE_LIST", roomTypeList);
            request.setAttribute("HOTEL_NAME", hotelName);
            request.setAttribute("HOTEL_ID", hotelId);
            request.setAttribute("CHECK_IN", dateFormat.format(parsedCheckInDate));
            request.setAttribute("CHECK_OUT", dateFormat.format(parsedCheckOutDate));
            request.setAttribute("TOTAL", total);
            request.setAttribute("PAGE_SIZE", pageSize);

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
