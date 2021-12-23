/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.room_type_price;

/**
 *
 * @author PC
 */
public class RoomTypePriceDTO {
    private int hotelId;
    private int roomTypeId;
    private float price;

    public RoomTypePriceDTO(int hotelId, int roomTypeId, float price) {
        this.hotelId = hotelId;
        this.roomTypeId = roomTypeId;
        this.price = price;
    }

    public RoomTypePriceDTO(float price) {
        this.price = price;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
    
}
