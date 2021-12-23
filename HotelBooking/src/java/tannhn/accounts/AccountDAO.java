/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tannhn.accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import javax.naming.NamingException;
import tannhn.helpers.DBHelper;
import tannhn.roles.RoleDTO;

/**
 *
 * @author PC
 */
public class AccountDAO {

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

    public AccountDTO checkLogin(String userId, String password) throws NamingException, SQLException {
        con = DBHelper.getConnect();
        try {
            String sql = "SELECT a.[Id] as UserId, r.[Id] as RoleID, a.[Name] as UserName, [Address], r.[Name] as RoleName "
                    + "FROM [dbo].[Account] a LEFT JOIN [dbo].[Role] r on a.RoleId = r.Id "
                    + " WHERE a.[Id] = ? AND a.[Password] = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            stm.setString(2, password);
            rs = stm.executeQuery();
            if (rs.next()) {
                return new AccountDTO(rs.getString("UserId"),
                        rs.getString("UserName"),
                        rs.getString("Address"),
                        new RoleDTO(rs.getInt("RoleID"), rs.getString("RoleName"))
                );
            }
        } finally {
            closeDB();
        }
        return null;
    }

    public boolean register(AccountDTO account) throws NamingException, SQLException {
        boolean check = false;
        con = DBHelper.getConnect();
        try {
            Date date = new Date();
            String insertAccountSQL = "INSERT INTO [dbo].[Account]([Id],[Name],[Password],[Address],[Phone], [RoleId], [AccountStatusId], [CreateDate])"
                    + "VALUES (?,?,?,?,?,?,?, ?)";
            stm = con.prepareStatement(insertAccountSQL);
            stm.setString(1, account.getId());
            stm.setString(2, account.getName());
            stm.setString(3, account.getPassword());
            stm.setString(4, account.getAddress());
            stm.setString(5, account.getPhone());
            stm.setInt(6, account.getRoleId());
            stm.setInt(7, account.getAccountStatusId());
            stm.setTimestamp(8, new Timestamp(date.getTime()));
            check = stm.executeUpdate() > 0;
        } finally {
            closeDB();
        }
        return check;
    }

    public boolean checkDuplicateUserId(String userId) throws NamingException, SQLException {
        boolean isDuplicate = false;
        con = DBHelper.getConnect();
        try {
            String sql = "SELECT [Id] FROM [dbo].[Account] WHERE [Id] = ?";
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            rs = stm.executeQuery();
            if (rs.next()) {
                isDuplicate = true;
            }
        } finally {
            closeDB();
        }
        return isDuplicate;
    }
}
