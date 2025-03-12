package com.megacitycab.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

class DriverDAOImplementationTest {

    private DriverDAOImplementation driverDAO;
    private UserDAOImplementation userDAO;
    private Driver testDriver;
    private User testUser;
    private Connection conn;

    @BeforeEach
    void setUp() {
        try {
            conn = DBConnectionFactory.getConnection();
            userDAO = new UserDAOImplementation(conn);
            driverDAO = new DriverDAOImplementation();

            // Clear database tables to ensure a clean slate
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM driver");
                stmt.execute("DELETE FROM users");
            }

            // Setup test user with unique email
            String uniqueEmail = "driver_" + UUID.randomUUID().toString() + "@exam.com";
            testUser = new User();
            testUser.setName("testDriver");
            testUser.setEmail(uniqueEmail);
            testUser.setPassword("password123");
            testUser.setRole(UserRole.DRIVER);

            // Setup test driver with unique license number
            String uniqueLicense = "LIC" + UUID.randomUUID().toString().substring(0, 8);
            testDriver = new Driver();
            testDriver.setName("testDriver");
            testDriver.setLicenseNumber(uniqueLicense);
            testDriver.setContactNumber("1234567890");
            testDriver.setPhoneNumber("0987654321");
            testDriver.setAddress("123 Test St");
            testDriver.setDriverStatus(DriverStatus.AVAILABLE);

        } catch (SQLException e) {
            fail("Failed to setup test: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // Additional cleanup to ensure no residual data
            try (Statement stmt = conn.createStatement()) {
            	stmt.execute("DELETE FROM cab");
            	stmt.execute("DELETE FROM driver");
                stmt.execute("DELETE FROM users");
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to tear down: " + e.getMessage());
        }
    }

    @Test
    void testAddDriver() {
        // Create a user
        int userId = userDAO.createUser(testUser);
        assertTrue(userId > 0, "User creation should succeed");
        testDriver.setUserID(userId);

        // Test adding driver
        boolean result = driverDAO.addDriver(testDriver);
        assertTrue(result, "Driver should be added successfully");

        // Verify
        Driver retrieved = driverDAO.getDriverByUserID(userId);
        assertNotNull(retrieved, "Driver should be retrievable");
        assertEquals(testDriver.getLicenseNumber(), retrieved.getLicenseNumber());
    }

    @Test
    void testRegisterDriver() {
        // Create a user
        int userId = userDAO.createUser(testUser);
        assertTrue(userId > 0, "User creation should succeed");

        // Test registering driver
        boolean result = driverDAO.registerDriver(testDriver, userId);
        assertTrue(result, "Driver registration should succeed");

        // Verify
        Driver retrieved = driverDAO.getDriverByUserID(userId);
        assertNotNull(retrieved, "Registered driver should be retrievable");
    }

    @Test
    void testGetDriverById() {
        // Setup
        int userId = userDAO.createUser(testUser);
        testDriver.setUserID(userId);
        driverDAO.addDriver(testDriver);
        Driver created = driverDAO.getDriverByUserID(userId);

        // Test
        Driver retrieved = driverDAO.getDriverById(created.getDriverID());
        assertNotNull(retrieved, "Driver should be found by ID");
        assertEquals(created.getDriverID(), retrieved.getDriverID());
    }

    @Test
    void testGetDriverByUserID() {
        // Setup
        int userId = userDAO.createUser(testUser);
        driverDAO.registerDriver(testDriver,userId);

        // Test
        Driver retrieved = driverDAO.getDriverByUserID(userId);
        assertNotNull(retrieved, "Driver should be found by userID");
        assertEquals(userId, retrieved.getUserID());
    }

    @Test
    void testGetDriverByEmail() {

        int userId = userDAO.createUser(testUser);
        driverDAO.registerDriver(testDriver, userId);


        System.out.println("DEBUG TEST: Created user with email: " + testUser.getEmail());
        System.out.println("DEBUG TEST: User ID: " + userId);

        // Test
        Driver retrieved = driverDAO.getDriverByEmail(testUser.getEmail());


        if (retrieved != null) {
            System.out.println("DEBUG TEST: Retrieved driver");
            System.out.println("DEBUG TEST: Driver email: " + retrieved.getEmail());
            System.out.println("DEBUG TEST: Driver userID: " + retrieved.getUserID());
            System.out.println("DEBUG TEST: Driver name: " + retrieved.getName());
        } else {
            System.out.println("DEBUG TEST: Retrieved driver is null!");
        }

        assertNotNull(retrieved, "Driver should be found by email");


        System.out.println("DEBUG TEST: Comparing emails:");
        System.out.println("DEBUG TEST: Expected: " + testUser.getEmail());
        System.out.println("DEBUG TEST: Actual: " + retrieved.getEmail());

        assertEquals(testUser.getEmail(), retrieved.getEmail(), "Email should match");
    }

    @Test
    void testGetAllDrivers() {
        // Setup
        int userId = userDAO.createUser(testUser);
        testDriver.setUserID(userId);
        driverDAO.addDriver(testDriver);

        // Test
        java.util.List<Driver> drivers = driverDAO.getAllDrivers();
        assertFalse(drivers.isEmpty(), "Drivers list should not be empty");
        assertTrue(drivers.stream().anyMatch(d -> d.getUserID() == userId),
                "Test driver should be in the list");
    }

    @Test
    void testUpdateDriver() {
        // Setup
        int userId = userDAO.createUser(testUser);

        driverDAO.registerDriver(testDriver,userId);

        	// Modify driver
            testDriver.setLicenseNumber("NEW" + UUID.randomUUID().toString().substring(0, 8));
            testDriver.setName("updatedDriver");
            boolean result = driverDAO.updateDriver(testDriver);
            assertTrue(result, "Update should succeed");

         // Verify
            Driver updated = driverDAO.getDriverByUserID(userId);
            assertEquals(testDriver.getLicenseNumber(), updated.getLicenseNumber());
            assertEquals("updatedDriver", updated.getName());
		}




    @Test
    void testDeleteDriver() {
        // Setup
        int userId = userDAO.createUser(testUser);
        testDriver.setUserID(userId);
        driverDAO.addDriver(testDriver);

        // Test
        boolean result = driverDAO.deleteDriver(userId);
        assertTrue(result, "Deletion should succeed");

        // Verify
        Driver deleted = driverDAO.getDriverByUserID(userId);
        assertNull(deleted, "Driver should be deleted");
        User deletedUser = userDAO.getUserById(userId);
        assertNull(deletedUser, "Associated user should be deleted");
    }
}