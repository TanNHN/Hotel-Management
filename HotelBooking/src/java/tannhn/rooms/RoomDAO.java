/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.rooms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tannhn.helpers.DBHelper;
import tannhn.hotels.HotelDTO;
import tannhn.room_types.RoomTypeDTO;

/**
 *
 * @author PC
 */
public class RoomDAO {

    private Connection con;
    private PreparedStatement stm;
    private ResultSet rs;

    private void closeDB() throws NamingException, SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stm != null) {
            stm.close();
        }
        if (con != null) {
            con.close();
        }
    }

    public List<RoomDTO> searchRoomForBooking(Date checkIn, Date checkOut, int roomTypeId, int hotelId, int quantity) throws NamingException, SQLException, Exception {
        List<RoomDTO> roomList = new ArrayList<>();
        List<Integer> roomIdList = new ArrayList<>();
        con = DBHelper.getConnect();
        try {
//            if (roomTypeId == 0) {
//                String sql = "SELECT r.Id FROM [dbo].[Room] r "
//                        + "WHERE r.Id NOT IN  ( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
//                        + "((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
//                        + "(bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
//                        + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.[HotelId] = ?"
//                        + " ORDER BY r.Id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//                stm = con.prepareStatement(sql);
//                stm.setTimestamp(1, new Timestamp(checkIn.getTime()));
//                stm.setTimestamp(2, new Timestamp(checkOut.getTime()));
//                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
//                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
//                stm.setInt(5, hotelId);
//                stm.setInt(6, skip);
//                stm.setInt(7, pageSize);
//
//            } else {
            String sql = "SELECT TOP (?) r.Id FROM [dbo].[Room] r "
                    + "WHERE r.Id NOT IN ( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                    + "((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                    + "(bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                    + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.RoomTypeId = ? AND r.[HotelId] = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, quantity);
            stm.setTimestamp(2, new Timestamp(checkIn.getTime()));
            stm.setTimestamp(3, new Timestamp(checkOut.getTime()));
            stm.setTimestamp(4, new Timestamp(checkIn.getTime()));
            stm.setTimestamp(5, new Timestamp(checkOut.getTime()));
            stm.setInt(6, roomTypeId);
            stm.setInt(7, hotelId);
//            }
            rs = stm.executeQuery();

            while (rs.next()) {
                roomIdList.add(rs.getInt("Id"));
            }
            for (Integer id : roomIdList) {
                RoomDTO room = new RoomDTO();
                String getRoomDetailsSQL = "SELECT r.Id, r.[Name] AS RoomName, rt.[Name] AS RoomTypeName, "
                        + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                        + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                        + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                        + "WHERE r.Id = ?";
                stm = con.prepareStatement(getRoomDetailsSQL);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()) {
                    room = new RoomDTO(rs.getInt("Id"),
                            rs.getString("RoomName"),
                            new HotelDTO(rs.getInt("HotelId"), rs.getString("HotelName"), rs.getString("Address")),
                            new RoomTypeDTO(rs.getInt("RoomtypeId"), rs.getString("RoomtypeName")));
                }
                roomList.add(room);
            }
//            while (rs.next()) {
//                roomList.add(new RoomDTO(rs.getInt("Id"),
//                        rs.getString("RoomName"),
//                        rs.getFloat("Price"),
//                        new HotelDTO(rs.getInt("HotelId"), rs.getString("HotelName"), rs.getString("Address")),
//                        checkIn,
//                        checkOut,
//                        new RoomTypeDTO(rs.getInt("RoomtypeId"), rs.getString("RoomtypeName"))));
//            }
        } finally {
            closeDB();
        }
        return roomList;
    }

    public int searchTotalRsoomForBooking(Date checkIn, Date checkOut, int roomTypeId, int hotelId) throws NamingException, SQLException, Exception {
        int total = 0;
        con = DBHelper.getConnect();
        try {
            if (roomTypeId == 0) {
                String sql = "SELECT COUNT(r.[Id]) AS Total FROM [dbo].[Room] r "
                        + "WHERE r.Id NOT IN  ( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + "((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + "(bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.[HotelId] = ?";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(2, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setInt(5, hotelId);

            } else {
                String sql = "SELECT COUNT(r.[Id]) as Total FROM [dbo].[Room] r "
                        + "WHERE r.Id NOT IN ( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + "((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + "(bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.RoomTypeId = ? AND r.[HotelId] = ?";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(2, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setInt(5, roomTypeId);
                stm.setInt(6, hotelId);
            }
            rs = stm.executeQuery();

            if (rs.next()) {
                total = rs.getInt("Total");

            }
        } finally {
            closeDB();
        }
        return total;
    }

    public List<RoomDTO> searchRoom(int hotelId, int roomTypeId, String checkIn, String checkOut, String roomName, String status) throws NamingException, SQLException, ParseException {
        List<RoomDTO> roomList = new ArrayList<>();
        con = DBHelper.getConnect();
        try {
            if (status.equals("booked")) {

                if (checkIn != null && checkOut == null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date parsedDate = dateFormat.parse(checkIn);
                    Timestamp checkInTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                    Timestamp checkin = checkInTimestamp;

                    if (roomTypeId == 0) {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckIn = ? AND r.[Name] LIKE ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkin);
                        stm.setString(3, "%" + roomName + "%");
                        rs = stm.executeQuery();
                    } else {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckIn = ? AND r.[Name] LIKE ?"
                                + " AND r.[RoomTypeId] = ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkin);
                        stm.setString(3, "%" + roomName + "%");
                        stm.setInt(4, roomTypeId);

                        rs = stm.executeQuery();
                    }
                }
                if (checkOut != null && checkIn == null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date parsedDate = dateFormat.parse(checkOut);
                    Timestamp checkOutTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                    Timestamp checkout = checkOutTimestamp;
                    if (roomTypeId == 0) {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut = ? AND r.[Name] LIKE ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkout);
                        stm.setString(3, "%" + roomName + "%");
                        rs = stm.executeQuery();
                    } else {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut = ? AND r.[Name] LIKE ?"
                                + " AND r.[RoomTypeId] = ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkout);
                        stm.setString(3, "%" + roomName + "%");
                        stm.setInt(4, roomTypeId);

                        rs = stm.executeQuery();
                    }
                }
                if (checkIn != null && checkOut != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date parsedCheckInDate = dateFormat.parse(checkIn);
                    Date parsedCheckOutDate = dateFormat.parse(checkOut);
                    Timestamp checkOutTimestamp = new java.sql.Timestamp(parsedCheckOutDate.getTime());
                    Timestamp checkInTimestamp = new java.sql.Timestamp(parsedCheckInDate.getTime());
                    Timestamp checkin = checkInTimestamp;
                    Timestamp checkout = checkOutTimestamp;

                    if (roomTypeId == 0) {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut = ? AND r.CheckIn= ? AND r.[Name] LIKE ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkout);
                        stm.setTimestamp(3, checkin);
                        stm.setString(4, "%" + roomName + "%");
                        rs = stm.executeQuery();
                    } else {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut = ? AND r.CheckIn= ? AND r.[Name] LIKE ?"
                                + " AND r.[RoomTypeId] = ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setTimestamp(2, checkout);
                        stm.setTimestamp(3, checkin);
                        stm.setString(4, "%" + roomName + "%");
                        stm.setInt(5, roomTypeId);

                        rs = stm.executeQuery();
                    }
                }
                if (checkIn == null && checkOut == null) {
                    if (roomTypeId == 0) {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut IS NOT NULL AND r.CheckIn IS NOT NULL AND r.[Name] LIKE ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setString(2, "%" + roomName + "%");
                        rs = stm.executeQuery();
                    } else {
                        String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                                + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                                + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                                + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                                + "WHERE r.HotelId = ? AND r.CheckOut IS NOT NULL AND r.CheckIn IS NOT NULL AND r.[Name] LIKE ?"
                                + " AND r.[RoomTypeId] = ?";
                        stm = con.prepareStatement(sql);
                        stm.setInt(1, hotelId);
                        stm.setString(2, "%" + roomName + "%");

                        stm.setInt(3, roomTypeId);

                        rs = stm.executeQuery();
                    }
                }
            } else {
                if (roomTypeId == 0) {
                    String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                            + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                            + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                            + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                            + "WHERE r.HotelId = ? AND r.CheckIn IS NULL AND r.[Name] LIKE ?";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, hotelId);
                    stm.setString(2, "%" + roomName + "%");
                    rs = stm.executeQuery();
                } else {
                    String sql = "SELECT r.Id, r.[Name] AS RoomName, r.CheckIn, r.CheckOut, rt.[Name] AS RoomTypeName, "
                            + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                            + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                            + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                            + "WHERE r.HotelId = ? AND r.CheckIn IS NULL AND r.[Name] LIKE ?"
                            + " AND r.[RoomTypeId] = ?";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, hotelId);
                    stm.setString(2, "%" + roomName + "%");

                    stm.setInt(3, roomTypeId);

                    rs = stm.executeQuery();
                }
            }
            while (rs.next()) {
                Date checkInDate = new Date();
                Date checkOutDate = new Date();
                if (rs.getTimestamp("CheckIn") == null) {
                    checkInDate = null;
                } else {
                    checkInDate.setTime(rs.getTimestamp("CheckIn").getTime());
                }
                if (rs.getTimestamp("CheckOut") == null) {
                    checkOutDate = null;
                } else {
                    checkOutDate.setTime(rs.getTimestamp("CheckOut").getTime());
                }
                roomList.add(new RoomDTO(rs.getInt("Id"),
                        rs.getString("RoomName"),
                        new HotelDTO(rs.getInt("HotelId"), rs.getString("HotelName"), rs.getString("Address")),
                        new RoomTypeDTO(rs.getInt("RoomtypeId"), rs.getString("RoomtypeName"))));
            }
        } finally {
            closeDB();
        }
        return roomList;
    }

    public RoomDTO getRoomDetails(int roomId) throws NamingException, SQLException, Exception {
        con = DBHelper.getConnect();
        RoomDTO room = new RoomDTO();
        try {
            String sql = "SELECT r.Id, r.[Name] AS RoomName, rt.[Name] AS RoomTypeName, "
                    + "h.Id AS HotelId, h.[Name] AS HotelName, h.[Address], "
                    + "rt.Id AS RoomtypeId FROM [dbo].[Room] r LEFT JOIN [dbo].[RoomType] rt on r.RoomTypeId = rt.Id "
                    + "LEFT JOIN [dbo].[Hotel] h on r.HotelId = h.Id "
                    + "WHERE r.Id = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, roomId);
            rs = stm.executeQuery();
            if (rs.next()) {
                room = new RoomDTO(rs.getInt("Id"),
                        rs.getString("RoomName"),
                        new HotelDTO(rs.getInt("HotelId"), rs.getString("HotelName"), rs.getString("Address")),
                        new RoomTypeDTO(rs.getInt("RoomtypeId"), rs.getString("RoomtypeName")));
            }
        } finally {
            closeDB();
        }
        return room;
    }

    
    
}
