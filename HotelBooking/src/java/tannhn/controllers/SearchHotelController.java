/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import tannhn.hotels.HotelDAO;
import tannhn.hotels.HotelDTO;

/**
 *
 * @author PC
 */
public class SearchHotelController extends HttpServlet {

    private static Logger LOGGER = Logger.getLogger(SearchHotelController.class);
    private static String SEARCH_HOTEL_PAGE = "hotel_search.jsp";
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
        String url = SEARCH_HOTEL_PAGE;
        String name = "";
        String address = "";
        HotelDAO hotelDAO = new HotelDAO();
        int skip = 0;
        int total = 0;
        int pageSize = 20;
        int page = 1;
        String checkIn = null;
        String checkOut = null;
        int totalRoom = 0;
        Calendar cal = new GregorianCalendar();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date parsedCheckInDate = new Date();
        Date parsedCheckOutDate = new Date();
        Date startCheckInDate = new Date();
        Date endCheckOutDate = new Date();;
        try {
            if (request.getParameter("txtName") != null) {
                name = request.getParameter("txtName");
            }
            if (request.getParameter("txtAddress") != null) {
                address = request.getParameter("txtAddress");
            }
            if (request.getParameter("slPageSize") != null) {
                if (request.getParameter("slPageSize").length() > 0) {
                    pageSize = Integer.parseInt(request.getParameter("slPageSize"));
                }
            }
            if (request.getParameter("txtTotalRoom") != null) {
                if (request.getParameter("txtTotalRoom").length() > 0) {
                    totalRoom = Integer.parseInt(request.getParameter("txtTotalRoom"));
                }
            }
            if (request.getParameter("page") == null) {
                page = 1;
                skip = 0;
            } else {
                page = Integer.parseInt(request.getParameter("page"));
                skip = (page - 1) * pageSize;
            }
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
            cal.setTime(parsedCheckInDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startCheckInDate = cal.getTime();
            cal.setTime(parsedCheckOutDate);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 59);
            endCheckOutDate = cal.getTime();
            List<HotelDTO> hotelList = hotelDAO.searchHotelWithRoom(name, address, totalRoom, startCheckInDate, endCheckOutDate, skip, pageSize);
            total = hotelDAO.searchTotalHotel(name, address, totalRoom, startCheckInDate, endCheckOutDate);
            request.setAttribute("HOTEL_LIST", hotelList);
            request.setAttribute("TOTAL", total);
            request.setAttribute("PAGE_SIZE", pageSize);
            request.setAttribute("CHECK_IN", dateFormat.format(parsedCheckInDate));
            request.setAttribute("CHECK_OUT", dateFormat.format(parsedCheckOutDate));
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
