/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.booking_details;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import tannhn.helpers.DBHelper;
import tannhn.room_type_price.RoomTypePriceDTO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class BookingDetailsDAO {

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

    public List<BookingDetailsDTO> getBookingDetails(int orderId) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        List<BookingDetailsDTO> bookingDetailsList = new ArrayList<>();
        try {
            String sql = "SELECT [BookingId],[RoomId],bd.[CheckIn],bd.[CheckOut],[Amount],"
                    + "r.Id AS RoomId, r.[Name],r.RoomTypeId,rt.[Name] AS RoomTypeName, rtp.Price "
                    + "FROM [dbo].[BookingDetails] bd  LEFT JOIN "
                    + "[dbo].[Room] r ON bd.RoomId = r.Id LEFT JOIN [dbo].[RoomType] rt "
                    + "ON r.RoomTypeId = rt.Id LEFT JOIN [dbo].[RoomTypePrice] rtp ON rtp.RoomTypeId = r.RoomTypeId "
                    + "WHERE bd.BookingId = ? AND rtp.HotelId = r.HotelId";
            stm = con.prepareStatement(sql);
            stm.setInt(1, orderId);
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
                                        rs.getString("RoomTypeName"),
                                        new RoomTypePriceDTO(rs.getFloat("Price")))
                        )));
            }
        } finally {
            closeDB();
        }
        return bookingDetailsList;
    }
}
