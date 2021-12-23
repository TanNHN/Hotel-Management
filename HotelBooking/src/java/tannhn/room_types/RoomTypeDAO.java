/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.room_types;

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
import tannhn.room_type_price.RoomTypePriceDTO;

/**
 *
 * @author PC
 */
public class RoomTypeDAO {

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

    public List<RoomTypeDTO> geAllRoomType() throws NamingException, SQLException {
        List<RoomTypeDTO> roomTypeList = new ArrayList<>();
        con = DBHelper.getConnect();
        try {
            String sql = "SELECT [Id], [Name] FROM [dbo].[RoomType]";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                roomTypeList.add(new RoomTypeDTO(rs.getInt("Id"), rs.getString("Name")));
            }
        } finally {
            closeDB();
        }
        return roomTypeList;
    }

    public List<RoomTypeDTO> getAllRoomFromRoomType(Date checkIn, Date checkOut, int hotelId) throws NamingException, SQLException {
        List<RoomTypeDTO> roomTypeList = new ArrayList<>();
        con = DBHelper.getConnect();
        try {
            String sql = "SELECT r.RoomTypeId, rtp.Price,rt.[Name], COUNT(r.id) AS TotalRoom FROM [dbo].[Room] r "
                    + "LEFT JOIN [dbo].[RoomType] rt ON r.RoomTypeId = rt.Id "
                    + "LEFT JOIN [dbo].[RoomTypePrice] rtp ON rtp.HotelId = r.HotelId AND rtp.RoomTypeId = r.RoomTypeId "
                    + "WHERE r.Id NOT IN  ( SELECT bd.RoomId FROM [dbo].[BookingDetails] bd WHERE "
                    + " ((bd.CheckOut >= ? AND bd.CheckIn <= ? ) OR "
                    + " (bd.CheckOut <= ? AND bd.CheckIn >= ? )) AND bd.BookingId IN "
                    + " (SELECT ID FROM [dbo].[Booking] WHERE [BookingStatusId] = 1)) AND r.HotelId = ? "
                    + "GROUP BY r.RoomTypeId, rt.[Id], rt.[Name], rtp.RoomTypeId, rtp.Price";
            stm = con.prepareStatement(sql);
            stm.setTimestamp(1, new Timestamp(checkIn.getTime()));
            stm.setTimestamp(2, new Timestamp(checkOut.getTime()));
            stm.setTimestamp(3, new Timestamp(checkIn.getTime()));
            stm.setTimestamp(4, new Timestamp(checkOut.getTime()));
            stm.setInt(5, hotelId);
            rs = stm.executeQuery();
            while (rs.next()) {
                roomTypeList.add(new RoomTypeDTO(rs.getInt("RoomTypeId"),
                        rs.getString("Name"),
                        rs.getInt("TotalRoom"),
                        new RoomTypePriceDTO(rs.getFloat("Price"))
                ));
            }
        } finally {
            closeDB();
        }
        return roomTypeList;
    }
}
