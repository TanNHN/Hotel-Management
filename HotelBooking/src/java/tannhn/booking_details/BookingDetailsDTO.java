/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.booking_details;

import java.util.Date;
import tannhn.hotels.HotelDTO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class BookingDetailsDTO {

    private int bookingId, roomId;
    private Date checkIn, checkOut;
    private float amount;
    private RoomDTO room;

    public BookingDetailsDTO(int bookingId, Date checkIn, Date checkOut, float amount, RoomDTO roomDTO) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.room = roomDTO;
    }

    public BookingDetailsDTO(RoomDTO roomDTO, int roomId, Date checkIn, Date checkOut, float amount ) {
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.room = roomDTO;
    }

    
    
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO roomDTO) {
        this.room = roomDTO;
    }
    
    
    
}
