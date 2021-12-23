/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.hotels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tannhn.helpers.DBHelper;

/**
 *
 * @author PC
 */
public class HotelDAO {

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

    public List<HotelDTO> searchHotel(String name, String address, int skip, int pageSize) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        List<HotelDTO> hotelList = new ArrayList<>();
        try {
            String sql = "SELECT [Id],[Name],[Address] "
                    + "FROM [dbo].[Hotel] h "
                    + "WHERE h.[Name] LIKE ? AND h.[Address] LIKE ? ORDER BY h.Id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            stm = con.prepareCall(sql);
            stm.setString(1, "%" + name + "%");
            stm.setString(2, "%" + address + "%");
            stm.setInt(3, skip);
            stm.setInt(4, pageSize);

            rs = stm.executeQuery();
            while (rs.next()) {
                hotelList.add(new HotelDTO(rs.getInt("Id"), rs.getString("Name"), rs.getString("Address")));
            }
        } finally {
            closeDB();
        }
        return hotelList;
    }

    public List<HotelDTO> searchHotelWithRoom(String name, String address, int totalRoom, Date checkIn, Date checkOut, int skip, int pageSize) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        List<HotelDTO> hotelList = new ArrayList<>();
        try {
            if (totalRoom > 0) {
                String sql = "SELECT [Id],[Name],[Address] "
                        + "FROM [dbo].[Hotel] h "
                        + "WHERE h.[Name] LIKE ? AND h.[Address] LIKE ? AND h.ID IN (SELECT r.HotelId FROM [dbo].[Room] r "
                        + " WHERE r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) ) "
                        + "	AND (SELECT COUNT(r.[Id]) AS Total FROM [dbo].[Room] r "
                        + " WHERE r.HotelId = h.Id AND r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <=  ?) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1))) >= ? ORDER BY h.Id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + address + "%");
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(5, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(6, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(7, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(8, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(9, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(10, new Timestamp(checkOut.getTime()));
                stm.setInt(11, totalRoom);
                stm.setInt(12, skip);
                stm.setInt(13, pageSize);
            } else {
                String sql = "SELECT [Id],[Name],[Address] "
                        + "FROM [dbo].[Hotel] h "
                        + "WHERE h.[Name] LIKE ? AND h.[Address] LIKE ? AND h.ID IN (SELECT r.HotelId FROM [dbo].[Room] r "
                        + " WHERE r.HotelId = h.Id AND r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) ) "
                        + " ORDER BY h.Id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + address + "%");
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(5, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(6, new Timestamp(checkOut.getTime()));

                stm.setInt(7, skip);
                stm.setInt(8, pageSize);
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                hotelList.add(new HotelDTO(rs.getInt("Id"), rs.getString("Name"), rs.getString("Address")));
            }
        } finally {
            closeDB();
        }
        return hotelList;
    }

    public int searchTotalHotel(String name, String address, int totalRoom, Date checkIn, Date checkOut) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        int total = 0;
        try {
            if (totalRoom > 0) {
                String sql = "SELECT COUNT([Id]) as Total "
                        + "FROM [dbo].[Hotel] h "
                        + "WHERE h.[Name] LIKE ? AND h.[Address] LIKE ? AND h.ID IN (SELECT r.HotelId FROM [dbo].[Room] r "
                        + " WHERE r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) ) "
                        + "	AND (SELECT COUNT(r.[Id]) AS Total FROM [dbo].[Room] r "
                        + " WHERE r.HotelId = h.Id AND r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <=  ?) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1))) >= ?";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + address + "%");
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(5, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(6, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(7, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(8, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(9, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(10, new Timestamp(checkOut.getTime()));
                stm.setInt(11, totalRoom);

            } else {
                String sql = "SELECT COUNT([Id]) as Total "
                        + "FROM [dbo].[Hotel] h "
                        + "WHERE h.[Name] LIKE ? AND h.[Address] LIKE ? AND h.ID IN (SELECT r.HotelId FROM [dbo].[Room] r "
                        + " WHERE r.HotelId = h.Id AND r.Id NOT IN  (SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                        + " ((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                        + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                        + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) ) ";
                stm = con.prepareCall(sql);
                stm.setString(1, "%" + name + "%");
                stm.setString(2, "%" + address + "%");
                stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
                stm.setTimestamp(5, new Timestamp(checkIn.getTime()));
                stm.setTimestamp(6, new Timestamp(checkOut.getTime()));

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

    public HotelDTO getHotelDetails(int id) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        HotelDTO hotel = new HotelDTO();
        try {
            String sql = "SELECT Id, Name, Address FROM [dbo].[Hotel] WHERE Id = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            if (rs.next()) {
                hotel = new HotelDTO(rs.getInt("Id"), rs.getString("Name"), rs.getString("Address"));
            }
        } finally {
            closeDB();
        }
        return hotel;
    }

}
