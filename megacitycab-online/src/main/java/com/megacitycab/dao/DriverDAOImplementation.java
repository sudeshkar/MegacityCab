package com.megacitycab.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.UserService;

public class DriverDAOImplementation implements DriverDAO{
	private UserService userService;
	private Connection conn;


	public DriverDAOImplementation () {
		this.userService =UserService.getInstance();
		try {
			this.conn = DBConnectionFactory.getConnection();
		} catch (SQLException e) {

			e.printStackTrace();
		}
    }


    public DriverDAOImplementation(Connection conn) {
        this.userService = UserService.getInstance();
        this.conn = conn;
    }


    @Override
    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO driver (name, licenseNumber, contactNumber, phoneNumber, address, status, userID) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.setString(2, driver.getLicenseNumber());
            ps.setString(3, driver.getContactNumber());
            ps.setString(4, driver.getPhoneNumber());
            ps.setString(5, driver.getAddress());
            ps.setString(6, driver.getDriverStatus().toString());
            ps.setInt(7, driver.getUserID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding driver: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registerDriver(Driver driver, int userId) {

        String checkUserSQL = "SELECT COUNT(*) FROM users WHERE userID = ?";
        try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSQL)) {
            checkUserStmt.setInt(1, userId);
            System.out.println("user id in Driver Object: " + userId);

            ResultSet resultSet = checkUserStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {

                driver.setUserID(userId);

                String sql = "INSERT INTO driver (name, licenseNumber, contactNumber, phoneNumber, address, status, userID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, driver.getName());
                    ps.setString(2, driver.getLicenseNumber());
                    ps.setString(3, driver.getContactNumber());
                    ps.setString(4, driver.getPhoneNumber());
                    ps.setString(5, driver.getAddress());
                    ps.setString(6, driver.getDriverStatus().toString());
                    ps.setInt(7, userId);

                    int rowsAffected = ps.executeUpdate();
                    boolean success = rowsAffected > 0;
                    if (success) {
                        System.out.println("Driver registered successfully for userID: " + userId);
                    } else {
                        System.err.println("Failed to register driver for userID: " + userId);
                    }
                    return success;
                }
            } else {
                System.err.println("User with userID " + userId + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error registering driver for userID " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Driver getDriverById(int driverId) {
        String sql = "SELECT * FROM driver WHERE driverID = ?";
        Driver driver = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, driverId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    driver = extractDriverFromResultSet(rs);
                    System.out.println("Driver found for driverID: " + driverId);
                } else {
                    System.out.println("No driver found for driverID: " + driverId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching driver for driverID " + driverId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return driver;
    }

    @Override
    public Driver getDriverByUserID(int userId) {
        String sql = "SELECT * FROM driver WHERE userID = ?";
        Driver driver = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    driver = extractDriverFromResultSet(rs);
                    System.out.println("Driver found for userID: " + userId);
                } else {
                    System.out.println("No driver record found for userID: " + userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching driver for userID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return driver;
    }

    @Override
    public Driver getDriverByEmail(String email) {

        User user = null;
        String userSql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(userSql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setName(rs.getString("userName")); // or whatever column stores the user name
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(UserRole.valueOf(rs.getString("role")));

                    System.out.println("Found user with ID: " + user.getUserID() + ", email: " + user.getEmail());
                } else {
                    System.out.println("No user found with email: " + email);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            e.printStackTrace();
            return null;
        }


        if (user != null) {
            String driverSql = "SELECT * FROM driver WHERE userID = ?";

            try (PreparedStatement ps = conn.prepareStatement(driverSql)) {
                ps.setInt(1, user.getUserID());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Driver driver = new Driver();

                        driver.setUserID(user.getUserID());
                        driver.setName(user.getName());
                        driver.setEmail(user.getEmail());
                        driver.setPassword(user.getPassword());
                        driver.setRole(user.getRole());


                        driver.setDriverID(rs.getInt("driverID"));
                        driver.setLicenseNumber(rs.getString("licenseNumber"));
                        driver.setContactNumber(rs.getString("contactNumber"));
                        driver.setPhoneNumber(rs.getString("phoneNumber"));
                        driver.setAddress(rs.getString("address"));
                        driver.setDriverStatus(DriverStatus.valueOf(rs.getString("status")));

                        System.out.println("Found driver for user ID: " + user.getUserID());
                        return driver;
                    } else {
                        System.out.println("No driver record found for user ID: " + user.getUserID());
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error finding driver by user ID: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        List<User> allUsers = userService.getAllUser();

        if (allUsers == null || allUsers.isEmpty()) {
            System.out.println("No users found in the system");
            return drivers;
        }

        String sql = "SELECT * FROM driver WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (User user : allUsers) {
                if (user.getRole() == UserRole.DRIVER) {
                    ps.setInt(1, user.getUserID());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Driver driver = new Driver(
                                rs.getInt("driverID"),
                                user.getUserID(),
                                user.getName(),
                                user.getEmail(),
                                user.getPassword(),
                                user.getRole(),
                                rs.getString("licenseNumber"),
                                rs.getString("contactNumber"),
                                rs.getString("phoneNumber"),
                                rs.getString("address"),
                                DriverStatus.valueOf(rs.getString("status"))
                            );

                            drivers.add(driver);
                        } else {
                            System.out.println("Warning: User " + user.getUserID() +
                                              " has DRIVER role but no driver record found");
                        }
                    }
                }
            }
            System.out.println("Found " + drivers.size() + " drivers in total");
        } catch (SQLException e) {
            System.err.println("Error fetching all drivers: " + e.getMessage());
            e.printStackTrace();
        }

        return drivers;
    }

    @Override
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE driver SET name = ?, licenseNumber = ?, contactNumber = ?, " +
                     "phoneNumber = ?, address = ?, status = ? WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.setString(2, driver.getLicenseNumber());
            ps.setString(3, driver.getContactNumber());
            ps.setString(4, driver.getPhoneNumber());
            ps.setString(5, driver.getAddress());
            ps.setString(6, driver.getDriverStatus().toString());
            ps.setInt(7, driver.getUserID());

            int rowsAffected = ps.executeUpdate();
            boolean success = rowsAffected > 0;

            if (success) {
                System.out.println("Driver updated successfully for userID: " + driver.getUserID());
            } else {
                System.err.println("Failed to update driver for userID: " + driver.getUserID() +
                                  " - No matching record found");
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error updating driver for userID " + driver.getUserID() +
                              ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDriver(int userID) {
        // check  user exists and is a driver
        String roleCheckQuery = "SELECT role FROM users WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(roleCheckQuery)) {
            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    if (!"DRIVER".equalsIgnoreCase(role)) {
                        System.err.println("User " + userID + " is not a driver. Role: " + role);
                        return false;
                    }
                } else {
                    System.err.println("User not found for userID: " + userID);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking role for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }


        String deleteDriverQuery = "DELETE FROM driver WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteDriverQuery)) {
            ps.setInt(1, userID);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Driver record deleted for userID: " + userID);

                return deleteUser(userID);
            } else {
                System.err.println("No driver record found to delete for userID: " + userID);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting driver for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteUser(int userID) {
        String deleteUserQuery = "DELETE FROM users WHERE userID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteUserQuery)) {
            ps.setInt(1, userID);
            int rowsAffected = ps.executeUpdate();
            boolean success = rowsAffected > 0;

            if (success) {
                System.out.println("User deleted successfully for userID: " + userID);
            } else {
                System.err.println("Failed to delete user for userID: " + userID);
            }

            return success;
        } catch (SQLException e) {
            System.err.println("Error deleting user for userID " + userID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private Driver extractDriverFromResultSet(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setDriverID(rs.getInt("driverID"));
        driver.setName(rs.getString("name"));
        driver.setLicenseNumber(rs.getString("licenseNumber"));
        driver.setContactNumber(rs.getString("contactNumber"));
        driver.setPhoneNumber(rs.getString("phoneNumber"));
        driver.setAddress(rs.getString("address"));
        driver.setDriverStatus(DriverStatus.valueOf(rs.getString("status")));
        driver.setUserID(rs.getInt("userID"));
        return driver;
    }


}
