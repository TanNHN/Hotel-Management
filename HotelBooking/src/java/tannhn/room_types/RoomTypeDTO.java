/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.room_types;

import java.io.Serializable;
import java.util.List;
import tannhn.room_type_price.RoomTypePriceDTO;
import tannhn.rooms.RoomDTO;

/**
 *
 * @author PC
 */
public class RoomTypeDTO implements Serializable{
    private int id;
    private String name;
    private int totalRoom;
    private RoomTypePriceDTO roomTypePrice;
    private List<RoomDTO> roomList ;

    public RoomTypeDTO(int id, String name, int totalRoom, RoomTypePriceDTO roomTypePrice, List<RoomDTO> roomList) {
        this.id =id;
        this.name = name;
        this.totalRoom = totalRoom;
        this.roomTypePrice = roomTypePrice;
        this.roomList = roomList;
    }

    public RoomTypeDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    
    
    public RoomTypeDTO(int id, String name, RoomTypePriceDTO roomTypePriceDTO) {
        this.id = id;
        this.name = name;
        this.roomTypePrice = roomTypePriceDTO;
    }

    public RoomTypeDTO(int id, String name, int totalRoom, RoomTypePriceDTO roomTypePrice) {
        this.id = id;
        this.name = name;
        this.totalRoom = totalRoom;
        this.roomTypePrice = roomTypePrice;
    }



    public List<RoomDTO> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomDTO> roomList) {
        this.roomList = roomList;
    }

    
    
    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }

    public RoomTypePriceDTO getRoomTypePrice() {
        return roomTypePrice;
    }

    public void setRoomTypePrice(RoomTypePriceDTO roomTypePrice) {
        this.roomTypePrice = roomTypePrice;
    }

    
    
    public RoomTypeDTO() {
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
    
    
}
