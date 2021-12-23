/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.bookings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.naming.NamingException;
import tannh.booking_statues.BookingStatusDTO;
import tannhn.booking_details.BookingDetailsDAO;
import tannhn.booking_details.BookingDetailsDTO;
import tannhn.cart_item.CartItem;
import tannhn.helpers.DBHelper;
import tannhn.helpers.Helper;
import tannhn.hotels.HotelDTO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class BookingDAO {

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

    public List<Integer> insertBooking(List<BookingDetailsDTO> bookingDetails, String accountId, HashMap<Integer, Float> amountMap, int bookingStatusId) throws NamingException, SQLException {
        List<Integer> idList = new ArrayList<>();
        con = DBHelper.getConnect();
        int hotelId = 0;
        Date date = new Date();
        int bookingId = 0;
        try {
            for (int i = 0; i < bookingDetails.size(); i++) {
                if (bookingDetails.get(i).getRoom().getHotelId() == hotelId) {
                    String insertOrderDetailsSQL = "INSERT INTO [dbo].[BookingDetails]([BookingId],[RoomId],[CheckIn],[CheckOut],[Amount]) "
                            + "VALUES (?,?,?,?,?)";
                    stm = con.prepareStatement(insertOrderDetailsSQL);
                    stm.setInt(1, bookingId);
                    stm.setInt(2, bookingDetails.get(i).getRoomId());
                    stm.setTimestamp(3, new Timestamp(bookingDetails.get(i).getCheckIn().getTime()));
                    stm.setTimestamp(4, new Timestamp(bookingDetails.get(i).getCheckOut().getTime()));
                    stm.setFloat(5, bookingDetails.get(i).getAmount());

                    stm.executeUpdate();

                } else {
                    String insertBookingSQL = "INSERT INTO [dbo].[Booking]([AccountId],[HotelId],[CreateDate],[Total],[BookingStatusId])"
                            + " VALUES(?, ?, ?, ?, ?); SELECT SCOPE_IDENTITY()";
                    stm = con.prepareStatement(insertBookingSQL);
                    stm.setString(1, accountId);
                    stm.setInt(2, bookingDetails.get(i).getRoom().getHotelId());
                    System.out.println(bookingDetails.get(i).getRoom().getHotelId());
                    stm.setTimestamp(3, new Timestamp(date.getTime()));
                    stm.setFloat(4, amountMap.get(bookingDetails.get(i).getRoom().getHotelId()));
                    stm.setInt(5, bookingStatusId);

                    rs = stm.executeQuery();

                    if (rs.next()) {
                        idList.add(rs.getInt(1));
                        bookingId = rs.getInt(1);
                        String insertOrderDetailsSQL = "INSERT INTO [dbo].[BookingDetails]([BookingId],[RoomId],[CheckIn],[CheckOut],[Amount]) "
                                + "VALUES (?,?,?,?,?)";
                        stm = con.prepareStatement(insertOrderDetailsSQL);
                        stm.setInt(1, bookingId);
                        stm.setInt(2, bookingDetails.get(i).getRoomId());
                        stm.setTimestamp(3, new Timestamp(bookingDetails.get(i).getCheckIn().getTime()));
                        stm.setTimestamp(4, new Timestamp(bookingDetails.get(i).getCheckOut().getTime()));
                        stm.setFloat(5, bookingDetails.get(i).getAmount());
                        stm.executeUpdate();
                    }
                    hotelId = bookingDetails.get(i).getRoom().getHotelId();
                }
            }
        } finally {
            closeDB();
        }
        return idList;
    }

    public List<BookingDTO> searchBooking(String userId, String hotelName, Date bookingDate, int skip, int pageSize) throws NamingException, SQLException {
        List<BookingDTO> bookingList = new ArrayList<>();
        BookingDetailsDAO bookingDetailsDAO = new BookingDetailsDAO();
        con = DBHelper.getConnect();
        try {
            if (bookingDate == null) {
                String sql = "SELECT b.[Id], [CreateDate],b.[BookingStatusId], [AccountId], [HotelId], [Total], bs.[Name] AS BookingStatusName,h.[Address], h.[Name] AS HotelName FROM [dbo].[Booking] b LEFT JOIN"
                        + " [dbo].[Hotel] h ON b.HotelId = h.Id  LEFT JOIN [dbo].[BookingStatus] bs ON bs.Id = b.BookingStatusId"
                        + " WHERE h.[Name] LIKE ? AND [AccountId] = ? ORDER BY [CreateDate] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + hotelName + "%");
                stm.setString(2, userId);
                stm.setInt(3, skip);
                stm.setInt(4, pageSize);
            } else {
                Calendar cal = new GregorianCalendar();
                cal.setTime(bookingDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startDate = cal.getTime();
                cal = new GregorianCalendar();
                cal.setTime(bookingDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
                Date endDate = cal.getTime();
                String sql = "SELECT b.[Id], [CreateDate], [AccountId],b.[BookingStatusId], [HotelId],bs.[Name] AS BookingStatusName, [Total], h.[Address], h.[Name] AS HotelName FROM [dbo].[Booking] b LEFT JOIN"
                        + " [dbo].[Hotel] h ON b.HotelId = h.Id  LEFT JOIN [dbo].[BookingStatus] bs ON bs.Id = b.BookingStatusId"
                        + " WHERE [CreateDate] BETWEEN ? AND ? AND h.[Name] LIKE ? AND [AccountId] = ? ORDER BY [CreateDate] DESC"
                        + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(startDate.getTime()));
                stm.setTimestamp(2, new Timestamp(endDate.getTime()));
                stm.setString(3, "%" + hotelName + "%");
                stm.setString(4, userId);
                stm.setInt(5, skip);
                stm.setInt(6, pageSize);
            }

            rs = stm.executeQuery();
            while (rs.next()) {
                Date createDate = new Date();

                createDate.setTime(rs.getTimestamp("CreateDate").getTime());
                bookingList.add(new BookingDTO(rs.getInt("Id"), rs.getInt("HotelId"), userId, rs.getFloat("Total"), createDate,
                        new HotelDTO(rs.getInt("HotelId"), rs.getString("HotelName"), rs.getString("Address")),
                        bookingDetailsDAO.getBookingDetails(rs.getInt("Id")),
                        new BookingStatusDTO(rs.getInt("BookingStatusId"), rs.getString("BookingStatusName"))
                ));
            }
        } finally {
            closeDB();
        }
        return bookingList;
    }

    public int searchTotalBooking(String userId, String hotelName, Date bookingDate) throws NamingException, SQLException {
        int total = 0;
        con = DBHelper.getConnect();
        try {
            if (bookingDate == null) {
                String sql = "SELECT COUNT(b.[Id]) as Total FROM [dbo].[Booking] b LEFT JOIN"
                        + " [dbo].[Hotel] h ON b.HotelId = h.Id  LEFT JOIN [dbo].[BookingStatus] bs ON bs.Id = b.BookingStatusId"
                        + " WHERE h.[Name] LIKE ? AND [AccountId] = ?";
                stm = con.prepareStatement(sql);
                stm.setString(1, "%" + hotelName + "%");
                stm.setString(2, userId);

            } else {
                Calendar cal = new GregorianCalendar();
                cal.setTime(bookingDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startDate = cal.getTime();
                cal = new GregorianCalendar();
                cal.setTime(bookingDate);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
                Date endDate = cal.getTime();
                String sql = "SELECT COUNT(b.[Id]) as Total FROM [dbo].[Booking] b LEFT JOIN"
                        + " [dbo].[Hotel] h ON b.HotelId = h.Id  LEFT JOIN [dbo].[BookingStatus] bs ON bs.Id = b.BookingStatusId"
                        + " WHERE [CreateDate] BETWEEN ? AND ? AND h.[Name] LIKE ? AND [AccountId] = ? ";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(startDate.getTime()));
                stm.setTimestamp(2, new Timestamp(endDate.getTime()));

                stm.setString(3, "%" + hotelName + "%");
                stm.setString(4, userId);

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

    public boolean deactiveBooking(int bookingId) throws NamingException, SQLException {
        boolean check = false;
        con = DBHelper.getConnect();
        try {
            String sql = "UPDATE [dbo].[Booking] SET [BookingStatusId] = 2 WHERE [Id] = ?";
            stm = con.prepareStatement(sql);
            stm.setInt(1, bookingId);
            check = stm.executeUpdate() > 0;
        } finally {
        }
        return check;
    }

    public boolean insertCodeToBooking(int bookingId, String activateCode) throws NamingException, SQLException {
        boolean check = false;
        con = DBHelper.getConnect();
        try {
            String sql = "UPDATE [dbo].[Booking] SET [ActivateCode] = ? WHERE [Id] = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, activateCode);
            stm.setInt(2, bookingId);
            check = stm.executeUpdate() > 0;
        } finally {
        }
        return check;
    }

    public boolean activeBooking(int bookingId, String code) throws NamingException, SQLException {
        boolean check = false;
        con = DBHelper.getConnect();
        try {
            String checkBookingCodeSql = "SELECT ID FROM [dbo].[Booking] WHERE ID = ? AND ActivateCode = ? AND [BookingStatusId] = 3";
            stm = con.prepareStatement(checkBookingCodeSql);
            stm.setInt(1, bookingId);
            stm.setString(2, code);
            rs = stm.executeQuery();
            if (rs.next()) {
                String sql = "UPDATE [dbo].[Booking] SET [BookingStatusId] = 1 WHERE [Id] = ?";
                stm = con.prepareStatement(sql);
                stm.setInt(1, bookingId);
                check = stm.executeUpdate() > 0;
            }

        } finally {
            closeDB();
        }
        return check;
    }

    public List<RoomDTO> checkRoomStatus(List<BookingDetailsDTO> bookingDetails) throws NamingException, SQLException {
        List<RoomDTO> bookedRoomList = new ArrayList<>();
        con = DBHelper.getConnect();
        try {
            for (BookingDetailsDTO bookingDetail : bookingDetails) {
                Calendar cal = new GregorianCalendar();
                cal.setTime(bookingDetail.getCheckIn());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startCheckinDate = cal.getTime();
                cal = new GregorianCalendar();
                cal.setTime(bookingDetail.getCheckOut());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
                Date endCheckOutDate = cal.getTime();
                String sql = "SELECT [Id], [Name] FROM [dbo].[Room] r  WHERE r.Id  IN "
                        + "( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE bd.CheckIn BETWEEN ? AND ? "
                        + "AND bd.CheckOut BETWEEN ? AND ? AND bd.BookingId IN "
                        + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.[Id] = ?";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(startCheckinDate.getTime()));
                stm.setTimestamp(2, new Timestamp(endCheckOutDate.getTime()));
                stm.setTimestamp(3, new Timestamp(startCheckinDate.getTime()));
                stm.setTimestamp(4, new Timestamp(endCheckOutDate.getTime()));
                stm.setInt(5, bookingDetail.getRoomId());
                rs = stm.executeQuery();
                while (rs.next()) {
                    bookedRoomList.add(new RoomDTO(rs.getInt("Id"), rs.getString("Name")));
                }
            }
        } finally {
            closeDB();
        }
        return bookedRoomList;
    }

    public List<RoomDTO> checkBookingStatus(int bookingId) throws NamingException, SQLException {
        List<RoomDTO> bookedRoomList = new ArrayList<>();
        List<BookingDetailsDTO> bookingDetailsList = new ArrayList<>();

        con = DBHelper.getConnect();
        try {
            String getBookingDetailsSQL = "SELECT [BookingId],[RoomId],bd.[CheckIn],bd.[CheckOut],[Amount],"
                    + " r.Id AS RoomId, r.[Name],r.RoomTypeId,rt.[Name] AS RoomTypeName"
                    + " FROM [dbo].[BookingDetails] bd  LEFT JOIN "
                    + " [dbo].[Room] r ON bd.RoomId = r.Id LEFT JOIN [dbo].[RoomType] rt "
                    + " ON r.RoomTypeId = rt.Id WHERE bd.BookingId = ?";
            stm = con.prepareStatement(getBookingDetailsSQL);
            stm.setInt(1, bookingId);
            rs = stm.executeQuery();
            while (rs.next()) {
                Date checkInDate = new Date();
                Date checkOutDate = new Date();

                checkInDate.setTime(rs.getTimestamp("CheckIn").getTime());

                checkOutDate.setTime(rs.getTimestamp("CheckOut").getTime());
                bookingDetailsList.add(new BookingDetailsDTO(rs.getInt("BookingId"),
                        checkInDate, checkOutDate,
                        rs.getFloat("Amount"),
                        new RoomDTO(rs.getInt("RoomId"),
                                rs.getString("Name"),
                                new RoomTypeDTO(
                                        rs.getInt("RoomTypeId"),
                                        rs.getString("RoomTypeName"))
                        )));
            }
            for (BookingDetailsDTO bookingDetails : bookingDetailsList) {
                Calendar cal = new GregorianCalendar();
                cal.setTime(bookingDetails.getCheckIn());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startCheckinDate = cal.getTime();
                cal = new GregorianCalendar();
                cal.setTime(bookingDetails.getCheckOut());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 59);
                Date endCheckOutDate = cal.getTime();
                String sql = "(SELECT [Id], [Name] FROM [dbo].[Room] r  WHERE r.Id  IN "
                        + "( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE bd.CheckIn BETWEEN ? AND ? "
                        + "AND bd.CheckOut BETWEEN ? AND ? AND bd.BookingId IN "
                        + "(SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.Id = ?)";
                stm = con.prepareStatement(sql);
                stm.setTimestamp(1, new Timestamp(startCheckinDate.getTime()));
                stm.setTimestamp(2, new Timestamp(endCheckOutDate.getTime()));
                stm.setTimestamp(3, new Timestamp(startCheckinDate.getTime()));
                stm.setTimestamp(4, new Timestamp(endCheckOutDate.getTime()));
                stm.setInt(5, bookingDetails.getRoom().getId());
                rs = stm.executeQuery();
                while (rs.next()) {
                    bookedRoomList.add(new RoomDTO(rs.getInt("Id"), rs.getString("Name")));
                }
            }
        } finally {
            closeDB();
        }
        return bookedRoomList;
    }
}
