package com.megacitycab.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.megacitycab.model.Customer;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.mysql.cj.protocol.Resultset;

public class UserFactory {
    public static User createUser(ResultSet rs) throws SQLException {
        UserRole role = UserRole.valueOf(rs.getString("role"));

        switch (role) {
            case CUSTOMER:
                return new Customer(
                    rs.getInt("customerID"),
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    UserRole.valueOf(rs.getString("role")),
                    rs.getString("address"),
                    rs.getString("mobileNumber"),
                    rs.getString("phoneNumber"),
                    rs.getTimestamp("registrationDate").toLocalDateTime()
                );

            case DRIVER:
                return new Driver(
                    rs.getInt("driverID"),
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    role,
                    rs.getString("licenseNumber"),
                    rs.getString("contactNumber"),
                    rs.getString("phoneNumber"),
                    rs.getString("address"),
                    DriverStatus.valueOf(rs.getString("status"))
                );

            default:

                return new User(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("email"),
                    role
                );
        }
    }

    public static User setUser(Resultset rs) throws SQLException{
    	return null;
    }


}
