/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.rooms;

import java.io.Serializable;
import java.util.Date;
import tannhn.hotels.HotelDTO;
import tannhn.room_types.RoomTypeDTO;

/**
 *
 * @author PC
 */
public class RoomDTO implements Serializable{
    private int id, hotelId;
    private String name;
    private HotelDTO hotel;
    private RoomTypeDTO roomType;

    public RoomDTO(int id, String name, HotelDTO hotel, RoomTypeDTO roomType) {
        this.id = id;
        this.name = name;
        this.hotel = hotel;
        this.roomType = roomType;
    }

    public RoomDTO(int hotelId) {
        this.hotelId = hotelId;
    }

    public RoomDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoomDTO(int id, String name, RoomTypeDTO roomType) {
        this.id = id;
        this.name = name;
        this.roomType = roomType;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    public RoomDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HotelDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelDTO hotel) {
        this.hotel = hotel;
    }


    public RoomTypeDTO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeDTO roomType) {
        this.roomType = roomType;
    }
    
    
    
}
