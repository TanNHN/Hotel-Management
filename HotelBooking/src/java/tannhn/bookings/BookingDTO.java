/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.bookings;

import java.util.Date;
import java.util.List;
import tannh.booking_statues.BookingStatusDTO;
import tannhn.accounts.AccountDTO;
import tannhn.booking_details.BookingDetailsDTO;
import tannhn.hotels.HotelDTO;

/**
 *
 * @author PC
 */
public class BookingDTO {
    private int id, hotelId, bookingStatusId;
    private String accountId;
    private float total;
    private Date createDate;
    private AccountDTO account;
    private HotelDTO hotel;
    private List<BookingDetailsDTO> bookingDetails;
    private BookingStatusDTO bookingStatus;
    private String activateCode;
    public BookingDTO() {
    }

    public BookingDTO(int id, int hotelId, String accountId, float total, Date createDate, HotelDTO hotel, List<BookingDetailsDTO> bookingDetails, BookingStatusDTO bookingStatus) {
        this.id = id;
        this.hotelId = hotelId;
        this.accountId = accountId;
        this.total = total;
        this.createDate = createDate;
        this.hotel = hotel;
        this.bookingDetails = bookingDetails;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(int bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
    }

    public BookingStatusDTO getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatusDTO bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    
    
    public List<BookingDetailsDTO> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetailsDTO> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public HotelDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelDTO hotel) {
        this.hotel = hotel;
    }
    
    
    
    
}
