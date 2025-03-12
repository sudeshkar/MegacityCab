package com.megacitycab.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.CabService;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;

public class BookingDAOTest {
    private Connection conn;
    private BookingDAO bookingDAO;
    private Driver testDriver;
    private User testDriverUser;
    private User testCustomerUser;
    private Cab testCab;
    private Customer testCustomer;
    private CustomerService customerService;
    private CabService cabService;
    private DriverService driverService;
    private UserService userService;
    private UserDAOImplementation userDAO;
    private CustomerDAOImplementation customerDAO;
    private DriverDAOImplementation driverDAO;
    private CabDAOImplementation cabDAO;

    @BeforeEach
    void setUp() {
        try {
            conn = DBConnectionFactory.getConnection();

            // Initialize services
            userService = UserService.getInstance();
            customerService = CustomerService.getInstance();
            driverService = DriverService.getInstance();
            cabService = CabService.getInstance();

            // Initialize DAOs
            userDAO = new UserDAOImplementation(conn);
            driverDAO = new DriverDAOImplementation(conn);
            customerDAO = new CustomerDAOImplementation();
            cabDAO = new CabDAOImplementation(conn, driverDAO);

            // Initialize BookingDAO with services
            bookingDAO = new BookingDAO(conn, customerService, cabService, driverService, userService);

            // Clear database tables to ensure a clean slate
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM booking");  // Delete bookings first (depends on cab, customer, driver)
                stmt.execute("DELETE FROM cab");      // Delete cabs before drivers
                stmt.execute("DELETE FROM driver");   // Now safe to delete drivers
                stmt.execute("DELETE FROM customer"); // Delete customers
                stmt.execute("DELETE FROM users");    // Delete users last
            }

            // Setup and insert test data
            createAndPersistTestEntities();

        } catch (SQLException e) {
            fail("Failed to setup test: " + e.getMessage());
        }
    }

    private void createAndPersistTestEntities() {
        try {
            // Create unique identifiers
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            String driverEmail = "driver_" + uniqueId + "@exam.com";
            String customerEmail = "customer_" + uniqueId + "@exam.com";
            String licenseNumber = "LIC" + uniqueId;
            String vehicleNumber = "VEH" + uniqueId;

            // Create and persist driver user
            testDriverUser = new User();
            testDriverUser.setName("Test Driver");
            testDriverUser.setEmail(driverEmail);
            testDriverUser.setPassword("password123");
            testDriverUser.setRole(UserRole.DRIVER);
            int driverUserId = userDAO.createUser(testDriverUser);
            testDriverUser.setUserID(driverUserId);

            // Create and persist customer user
            testCustomerUser = new User();
            testCustomerUser.setName("Test Customer");
            testCustomerUser.setEmail(customerEmail);
            testCustomerUser.setPassword("password123");
            testCustomerUser.setRole(UserRole.CUSTOMER);
            int customerUserId = userDAO.createUser(testCustomerUser);
            testCustomerUser.setUserID(customerUserId);

            // Create and persist driver
            testDriver = new Driver();
            testDriver.setName("Test Driver");
            testDriver.setLicenseNumber(licenseNumber);
            testDriver.setContactNumber("1234567890");
            testDriver.setPhoneNumber("0987654321");
            testDriver.setAddress("123 Test St");
            testDriver.setDriverStatus(DriverStatus.AVAILABLE);
            testDriver.setUserID(driverUserId);
            driverDAO.addDriver(testDriver);
            testDriver = driverDAO.getDriverByUserID(driverUserId);


            // Create and persist customer
            testCustomer = new Customer();
            testCustomer.setUserID(customerUserId);
            testCustomer.setName("Test Customer");
            testCustomer.setEmail(customerEmail);
            testCustomer.setRole(UserRole.CUSTOMER);
            testCustomer.setAddress("456 Test Ave");
            testCustomer.setMobilenumber("9876543210");
            testCustomer.setPhonenumber("1234567890");
            testCustomer.setRegistrationDate(LocalDateTime.now());
            testCustomer.setStatus(CustomerStatus.ACTIVE);
            customerService.createCustomer(testCustomer);
            testCustomer = customerService.getCustomerByUserId(customerUserId);

            // Create and persist cab
            testCab = new Cab();
            testCab.setVehicleNumber(vehicleNumber);
            testCab.setModel("Test Model");
            testCab.setCategory(CabCategory.LUXURY);
            testCab.setCapacity(5);
            testCab.setCurrentLocation("Test Location");
            testCab.setCabStatus(CabStatus.AVAILABLE);
            testCab.setLastUpdated(LocalDateTime.now());
            testCab.setDriver(testDriver);
            cabDAO.addCab(testCab);
            testCab = cabService.getCabByDriverID(testDriver.getDriverID());


            // Verify entities are properly created
            assertNotNull(testDriverUser.getUserID(), "Driver user should have an ID");
            assertNotNull(testCustomerUser.getUserID(), "Customer user should have an ID");
            assertNotNull(testDriver.getDriverID(), "Driver should have an ID");
            assertNotNull(testCustomer.getCustomerID(), "Customer should have an ID");
            assertNotNull(testCab.getCabID(), "Cab should have an ID");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create test entities: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            // Additional cleanup to ensure no residual data
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM booking");  // Delete bookings first (depends on cab, customer, driver)
                stmt.execute("DELETE FROM cab");      // Delete cabs before drivers
                stmt.execute("DELETE FROM driver");   // Now safe to delete drivers
                stmt.execute("DELETE FROM customer"); // Delete customers
                stmt.execute("DELETE FROM users");    // Delete users last
            }
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to tear down: " + e.getMessage());
        }
    }

    @Test
    void addBookingTest() {
        // Create a booking with the persisted entities
        Booking booking = new Booking();
        booking.setCustomer(testCustomer);
        booking.setDriver(testDriver);
        booking.setCab(testCab);
        booking.setBookingDateTime(LocalDateTime.now());
        booking.setPickupLocation("Test Pickup");
        booking.setDestination("Test Destination");
        booking.setDistance(10.5);
        booking.setStatus(BookingStatus.PENDING);

        // Add the booking
        int bookingNumber = bookingDAO.addBooking(booking);
        System.out.println("Generated bookingNumber: " + bookingNumber);

        // Verify the booking was added successfully
        assertTrue(bookingNumber > 0, "Booking number should be positive");

        // Retrieve the booking and verify it matches what we created
        Booking retrievedBooking = bookingDAO.getBookingById(bookingNumber);
        assertNotNull(retrievedBooking, "Booking should be retrieved from DB");
        assertEquals(bookingNumber, retrievedBooking.getBookingNumber(), "Booking numbers should match");
        assertEquals(testCustomer.getCustomerID(), retrievedBooking.getCustomer().getCustomerID(), "Customer IDs should match");
        assertEquals(testDriver.getDriverID(), retrievedBooking.getDriver().getDriverID(), "Driver IDs should match");
        assertEquals(testCab.getCabID(), retrievedBooking.getCab().getCabID(), "Cab IDs should match");
    }

    @Test
    void shouldRetrieveAllBookings() throws SQLException {
        // Create and add a test booking first
        Booking booking = createTestBooking();
        int bookingNumber = bookingDAO.addBooking(booking);
        assertTrue(bookingNumber > 0, "Booking should be added successfully");

        // Get all bookings
        List<Booking> bookings = bookingDAO.getAllBookings();

        // Verify
        assertNotNull(bookings, "Bookings list should not be null");
        assertTrue(bookings.size() > 0, "Bookings list should have at least one entry");
        boolean found = false;
        for (Booking b : bookings) {
            if (b.getBookingNumber() == bookingNumber) {
                found = true;
                break;
            }
        }
        assertTrue(found, "The added booking should be in the list of all bookings");
    }

    @Test
    void shouldRetrieveBookingsByCustomerID() throws Exception {
        // Create and add a test booking first
        Booking booking = createTestBooking();
        int bookingNumber = bookingDAO.addBooking(booking);
        assertTrue(bookingNumber > 0, "Booking should be added successfully");

        // Get bookings by customer ID
        List<Booking> bookings = bookingDAO.getBookingByCustomerID(testCustomer.getCustomerID());

        // Verify
        assertNotNull(bookings, "Bookings list should not be null");
        assertTrue(bookings.size() > 0, "Bookings list should have at least one entry");
        boolean found = false;
        for (Booking b : bookings) {
            assertEquals(testCustomer.getCustomerID(), b.getCustomer().getCustomerID(),
                    "All bookings should be for the specified customer");
            if (b.getBookingNumber() == bookingNumber) {
                found = true;
            }
        }
        assertTrue(found, "The added booking should be in the list of customer bookings");
    }

    @Test
    void shouldDeleteBookingSuccessfully() {
        // Create and add a test booking first
        Booking booking = createTestBooking();
        int bookingNumber = bookingDAO.addBooking(booking);
        assertTrue(bookingNumber > 0, "Booking should be added successfully");

        // Delete the booking
        boolean deleted = bookingDAO.deleteBooking(bookingNumber);

        // Verify
        assertTrue(deleted, "Booking should be successfully deleted");
        Booking retrievedBooking = bookingDAO.getBookingById(bookingNumber);
        assertNull(retrievedBooking, "Booking should not be found after deletion");
    }

    @Test
    void shouldUpdateBookingSuccessfully() {
        // Create and add a test booking first
        Booking booking = createTestBooking();
        int bookingNumber = bookingDAO.addBooking(booking);
        assertTrue(bookingNumber > 0, "Booking should be added successfully");

        // Update the booking
        Booking retrievedBooking = bookingDAO.getBookingById(bookingNumber);
        retrievedBooking.setDestination("Updated Destination");
        boolean updated = bookingDAO.updateBooking(retrievedBooking);

        // Verify
        assertTrue(updated, "Booking should be successfully updated");
        Booking updatedBooking = bookingDAO.getBookingById(bookingNumber);
        assertEquals("Updated Destination", updatedBooking.getDestination(), "Destination should be updated");
    }

    @Test
    void shouldRetrieveBookingById() {
        // Create and add a test booking first
        Booking booking = createTestBooking();
        int bookingNumber = bookingDAO.addBooking(booking);
        assertTrue(bookingNumber > 0, "Booking should be added successfully");

        // Get booking by ID
        Booking retrievedBooking = bookingDAO.getBookingById(bookingNumber);

        // Verify
        assertNotNull(retrievedBooking, "Retrieved booking should not be null");
        assertEquals(bookingNumber, retrievedBooking.getBookingNumber(), "Booking number should match");
        assertEquals(testCustomer.getCustomerID(), retrievedBooking.getCustomer().getCustomerID(), "Customer should match");
        assertEquals(testDriver.getDriverID(), retrievedBooking.getDriver().getDriverID(), "Driver should match");
        assertEquals(testCab.getCabID(), retrievedBooking.getCab().getCabID(), "Cab should match");
    }

    private Booking createTestBooking() {
        Booking booking = new Booking();
        booking.setCustomer(testCustomer);
        booking.setDriver(testDriver);
        booking.setCab(testCab);
        booking.setBookingDateTime(LocalDateTime.now());
        booking.setPickupLocation("Test Pickup");
        booking.setDestination("Test Destination");
        booking.setDistance(10.5);
        booking.setStatus(BookingStatus.PENDING);
        return booking;
    }
}