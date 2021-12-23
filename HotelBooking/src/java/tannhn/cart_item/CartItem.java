/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.cart_item;

import java.io.Serializable;
import java.util.Date;
import tannhn.hotels.HotelDTO;
import tannhn.room_types.RoomTypeDTO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class CartItem implements Serializable {

    private HotelDTO hotel;
    private RoomTypeDTO roomType;
    private Date checkIn;
    private Date checkOut;
    private int available;
    private float amount;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public CartItem(HotelDTO hotel, RoomTypeDTO roomType, Date checkIn, Date checkOut, int quantity, float amount, int available) {
        this.hotel = hotel;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.quantity = quantity;
        this.available = available;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
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

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
