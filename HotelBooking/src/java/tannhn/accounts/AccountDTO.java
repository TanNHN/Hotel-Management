/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.accounts;

import java.io.Serializable;
import java.util.Date;
import tannhn.account_statuses.AccountStatusDTO;
import tannhn.roles.RoleDTO;

/**
 *
 * @author PC
 */
public class AccountDTO implements Serializable{
    private String id, name, password, address, phone;
    private int roleId, accountStatusId;
    private RoleDTO role;
    private Date createDate;
    private AccountStatusDTO accountStatus;
    
    public AccountDTO() {
    }

    public AccountDTO(String id, String name, String password, String phone, String address, int roleId, Date createDate, int accountStatusId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.roleId = roleId;
        this.accountStatusId = accountStatusId;
        this.phone = phone;
    }

    public AccountDTO(String id, String name, String address, RoleDTO role) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    public int getAccountStatusId() {
        return accountStatusId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccountStatusId(int accountStatusId) {
        this.accountStatusId = accountStatusId;
    }

    public AccountStatusDTO getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusDTO accountStatus) {
        this.accountStatus = accountStatus;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
    
    
}
