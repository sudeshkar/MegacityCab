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

import com.megacitycab.model.Bill;
import com.megacitycab.model.Booking;
import com.megacitycab.model.BookingStatus;
import com.megacitycab.model.Cab;
import com.megacitycab.model.CabCategory;
import com.megacitycab.model.CabStatus;
import com.megacitycab.model.Customer;
import com.megacitycab.model.CustomerStatus;
import com.megacitycab.model.Driver;
import com.megacitycab.model.DriverStatus;
import com.megacitycab.model.PaymentStatus;
import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;
import com.megacitycab.service.BookingService;
import com.megacitycab.service.CabService;
import com.megacitycab.service.CustomerService;
import com.megacitycab.service.DriverService;
import com.megacitycab.service.UserService;

public class BillDAOImplementationTest {
    private Connection conn;
    private BillDAOImplementation billDAO;
    private BookingDAO bookingDAO;
    private Driver testDriver;
    private User testDriverUser;
    private User testCustomerUser;
    private Cab testCab;
    private Customer testCustomer;
    private Booking testBooking;
    private Bill testBill;

    // Services
    private CustomerService customerService;
    private CabService cabService;
    private DriverService driverService;
    private UserService userService;
    private BookingService bookingService;

    // DAOs
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
            bookingService = BookingService.getInstance();

            // Initialize DAOs
            userDAO = new UserDAOImplementation(conn);
            driverDAO = new DriverDAOImplementation(conn);
            customerDAO = new CustomerDAOImplementation();
            cabDAO = new CabDAOImplementation(conn, driverDAO);

            // Initialize BookingDAO and BillDAO
            bookingDAO = new BookingDAO(conn, customerService, cabService, driverService, userService);
            billDAO = new BillDAOImplementation(conn);



            // Clear database tables to ensure a clean slate
            cleanDatabase();

            // Setup and insert test data
            createAndPersistTestEntities();

        } catch (SQLException e) {
            fail("Failed to setup test: " + e.getMessage());
        }
    }

    private void cleanDatabase() {
        try (Statement stmt = conn.createStatement()) {
            // Delete in proper order to respect foreign key constraints
            stmt.execute("DELETE FROM bill");        // Delete bills first
            stmt.execute("DELETE FROM booking");     // Then bookings
            stmt.execute("DELETE FROM cab");         // Then cabs
            stmt.execute("DELETE FROM driver");      // Then drivers
            stmt.execute("DELETE FROM customer");    // Then customers
            stmt.execute("DELETE FROM users");       // Users last
        } catch (SQLException e) {
            fail("Database cleanup failed: " + e.getMessage());
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

            // Create and persist booking
            testBooking = new Booking();
            testBooking.setCustomer(testCustomer);
            testBooking.setDriver(testDriver);
            testBooking.setCab(testCab);
            testBooking.setBookingDateTime(LocalDateTime.now());
            testBooking.setPickupLocation("Test Pickup");
            testBooking.setDestination("Test Destination");
            testBooking.setDistance(10.5);
            testBooking.setStatus(BookingStatus.COMPLETED); // Make sure it's completed so we can bill it

            int bookingNumber = bookingDAO.addBooking(testBooking);
            testBooking.setBookingNumber(bookingNumber);

            // Create test bill but don't persist it yet (we'll do that in tests)
            testBill = new Bill(
                0, // Will be auto-incremented
                testBooking,
                150.0, // Base amount
                15.0,  // Discount amount
                7.5,   // Tax amount
                142.5, // Total fare
                LocalDateTime.now(),
                PaymentStatus.PENDING
            );

            // Verify entities are properly created
            assertNotNull(testDriverUser.getUserID(), "Driver user should have an ID");
            assertNotNull(testCustomerUser.getUserID(), "Customer user should have an ID");
            assertNotNull(testDriver.getDriverID(), "Driver should have an ID");
            assertNotNull(testCustomer.getCustomerID(), "Customer should have an ID");
            assertNotNull(testCab.getCabID(), "Cab should have an ID");
            assertNotNull(testBooking.getBookingNumber(), "Booking should have a booking number");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create test entities: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            cleanDatabase();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to tear down: " + e.getMessage());
        }
    }

    @Test
    void testAddBill() {
        // Add the bill
        billDAO.addBill(testBill);

        // Retrieve all bills and check
        try {
            List<Bill> bills = billDAO.getAllBills();
            assertNotNull(bills, "Bills list should not be null");
            assertEquals(1, bills.size(), "There should be one bill");

            Bill retrievedBill = bills.get(0);
            assertNotNull(retrievedBill.getBillNumber(), "Bill should have a bill number");
            assertEquals(testBooking.getBookingNumber(), retrievedBill.getBooking().getBookingNumber(), "Booking numbers should match");
            assertEquals(testBill.getBaseAmount(), retrievedBill.getBaseAmount(), "Base amounts should match");
            assertEquals(testBill.getDiscountAmount(), retrievedBill.getDiscountAmount(), "Discount amounts should match");
            assertEquals(testBill.getTaxAmount(), retrievedBill.getTaxAmount(), "Tax amounts should match");
            assertEquals(testBill.getTotalFare(), retrievedBill.getTotalFare(), "Total fares should match");
            assertEquals(testBill.getPaymentStatus(), retrievedBill.getPaymentStatus(), "Payment statuses should match");
        } catch (SQLException e) {
            fail("Failed to retrieve bills: " + e.getMessage());
        }
    }

    @Test
    void testGetBillByNumber() {
        // Add the bill
        billDAO.addBill(testBill);

        // Get all bills to find the bill number
        try {
            List<Bill> bills = billDAO.getAllBills();
            int billNumber = bills.get(0).getBillNumber();

            // Get bill by number
            Bill retrievedBill = billDAO.getBillByNumber(billNumber);

            // Verify
            assertNotNull(retrievedBill, "Retrieved bill should not be null");
            assertEquals(billNumber, retrievedBill.getBillNumber(), "Bill numbers should match");
            assertEquals(testBooking.getBookingNumber(), retrievedBill.getBooking().getBookingNumber(), "Booking numbers should match");
            assertEquals(testBill.getBaseAmount(), retrievedBill.getBaseAmount(), "Base amounts should match");
            assertEquals(testBill.getPaymentStatus(), retrievedBill.getPaymentStatus(), "Payment statuses should match");
        } catch (SQLException e) {
            fail("Failed to get bill by number: " + e.getMessage());
        }
    }

    @Test
    void testGetAllBills() {
        // Add multiple bills
        billDAO.addBill(testBill);

        // Create a second bill with different values
        Bill secondBill = new Bill(
            0,
            testBooking,
            200.0,
            20.0,
            10.0,
            190.0,
            LocalDateTime.now().plusDays(1),
            PaymentStatus.PAID
        );
        billDAO.addBill(secondBill);

        // Get all bills
        try {
            List<Bill> bills = billDAO.getAllBills();

            // Verify
            assertNotNull(bills, "Bills list should not be null");
            assertEquals(2, bills.size(), "There should be two bills");
        } catch (SQLException e) {
            fail("Failed to get all bills: " + e.getMessage());
        }
    }

    @Test
    void testUpdatePaymentStatus() {
        // Add the bill
        billDAO.addBill(testBill);

        // Get all bills to find the bill number
        try {
            List<Bill> bills = billDAO.getAllBills();
            int billNumber = bills.get(0).getBillNumber();

            // Update payment status
            boolean updated = billDAO.updatePaymentStatus(billNumber, PaymentStatus.PAID);

            // Verify
            assertTrue(updated, "Payment status should be updated successfully");

            // Get the updated bill
            Bill updatedBill = billDAO.getBillByNumber(billNumber);
            assertEquals(PaymentStatus.PAID, updatedBill.getPaymentStatus(), "Payment status should be updated to PAID");
        } catch (SQLException e) {
            fail("Failed to update payment status: " + e.getMessage());
        }
    }

    @Test
    void testUpdateBill() {
        // Add the bill
        billDAO.addBill(testBill);

        // Get all bills to find the bill number
        try {
            List<Bill> bills = billDAO.getAllBills();
            Bill billToUpdate = bills.get(0);

            // Update bill
            billToUpdate.setBaseAmount(250.0);
            billToUpdate.setDiscountAmount(25.0);
            billToUpdate.setTaxAmount(12.5);
            billToUpdate.setTotalFare(237.5);
            billToUpdate.setPaymentStatus(PaymentStatus.PAID);

            boolean updated = billDAO.updateBill(billToUpdate);

            // Verify
            assertTrue(updated, "Bill should be updated successfully");

            // Get the updated bill
            Bill updatedBill = billDAO.getBillByNumber(billToUpdate.getBillNumber());
            assertEquals(250.0, updatedBill.getBaseAmount(), "Base amount should be updated");
            assertEquals(25.0, updatedBill.getDiscountAmount(), "Discount amount should be updated");
            assertEquals(12.5, updatedBill.getTaxAmount(), "Tax amount should be updated");
            assertEquals(237.5, updatedBill.getTotalFare(), "Total fare should be updated");
            assertEquals(PaymentStatus.PAID, updatedBill.getPaymentStatus(), "Payment status should be updated");
        } catch (SQLException e) {
            fail("Failed to update bill: " + e.getMessage());
        }
    }

    @Test
    void testDeleteBill() {
        // Add the bill
        billDAO.addBill(testBill);

        // Get all bills to find the bill number
        try {
            List<Bill> bills = billDAO.getAllBills();
            int billNumber = bills.get(0).getBillNumber();

            // Delete the bill
            boolean deleted = billDAO.deleteBill(billNumber);

            // Verify
            assertTrue(deleted, "Bill should be deleted successfully");

            // Try to get the deleted bill
            Bill deletedBill = billDAO.getBillByNumber(billNumber);
            assertNull(deletedBill, "Bill should not exist after deletion");
        } catch (SQLException e) {
            fail("Failed to delete bill: " + e.getMessage());
        }
    }

    @Test
    void testGetBillsByDateRange() {
        // Add bills with different dates
        LocalDateTime now = LocalDateTime.now();

        // Set specific dates for testing
        testBill.setBillDate(now);
        billDAO.addBill(testBill);

        // Create a second bill with a different date
        Bill secondBill = new Bill(
            0,
            testBooking,
            200.0,
            20.0,
            10.0,
            190.0,
            now.plusDays(2), // Two days later
            PaymentStatus.PENDING
        );
        billDAO.addBill(secondBill);

        try {
            // Test date range that includes only the first bill
            List<Bill> billsInRange1 = billDAO.getBillsByDateRange(
                now.minusHours(1),
                now.plusHours(1)
            );

            assertNotNull(billsInRange1, "Bills list should not be null");
            assertEquals(1, billsInRange1.size(), "There should be one bill in the first date range");

            // Test date range that includes both bills
            List<Bill> billsInRange2 = billDAO.getBillsByDateRange(
                now.minusHours(1),
                now.plusDays(3)
            );

            assertNotNull(billsInRange2, "Bills list should not be null");
            assertEquals(2, billsInRange2.size(), "There should be two bills in the second date range");

            // Test date range that includes no bills
            List<Bill> billsInRange3 = billDAO.getBillsByDateRange(
                now.minusDays(3),
                now.minusDays(2)
            );

            assertNotNull(billsInRange3, "Bills list should not be null");
            assertEquals(0, billsInRange3.size(), "There should be no bills in the third date range");
        } catch (SQLException e) {
            fail("Failed to get bills by date range: " + e.getMessage());
        }
    }

    @Test
    void testGetTotalRevenue() {
        // Add bills with different payment statuses
        testBill.setPaymentStatus(PaymentStatus.PAID);
        billDAO.addBill(testBill);

        // Create a second bill with PENDING status
        Bill pendingBill = new Bill(
            0,
            testBooking,
            200.0,
            20.0,
            10.0,
            190.0,
            LocalDateTime.now(),
            PaymentStatus.PENDING
        );
        billDAO.addBill(pendingBill);

        // Create a third bill with PAID status
        Bill anotherPaidBill = new Bill(
            0,
            testBooking,
            300.0,
            30.0,
            15.0,
            285.0,
            LocalDateTime.now(),
            PaymentStatus.PAID
        );
        billDAO.addBill(anotherPaidBill);

        try {
            // Get total revenue (should include only PAID bills)
            double totalRevenue = billDAO.getTotalRevenue();

            // Expected total: 142.5 + 285.0 = 427.5
            assertEquals(427.5, totalRevenue, 0.01, "Total revenue should be the sum of PAID bills only");
        } catch (SQLException e) {
            fail("Failed to get total revenue: " + e.getMessage());
        }
    }

    @Test
    void testGetBillByBookingNumber() {
        // Add the bill
        billDAO.addBill(testBill);

        try {
            // Get bill by booking number
            Bill retrievedBill = billDAO.getBillByBookingNumber(testBooking.getBookingNumber());

            // Verify
            assertNotNull(retrievedBill, "Retrieved bill should not be null");
            assertEquals(testBooking.getBookingNumber(), retrievedBill.getBooking().getBookingNumber(), "Booking numbers should match");
        } catch (SQLException e) {
            fail("Failed to get bill by booking number: " + e.getMessage());
        }
    }

    @Test
    void testGetBillsByPaymentStatus() {
        // Add bills with different payment statuses
        testBill.setPaymentStatus(PaymentStatus.PENDING);
        billDAO.addBill(testBill);

        // Create a second bill with PAID status
        Bill paidBill = new Bill(
            0,
            testBooking,
            200.0,
            20.0,
            10.0,
            190.0,
            LocalDateTime.now(),
            PaymentStatus.PAID
        );
        billDAO.addBill(paidBill);

        // Create a third bill with FAILED status
        Bill failedBill = new Bill(
            0,
            testBooking,
            300.0,
            30.0,
            15.0,
            285.0,
            LocalDateTime.now(),
            PaymentStatus.FAILED
        );
        billDAO.addBill(failedBill);

        try {
            // Get bills by payment status
            List<Bill> pendingBills = billDAO.getBillsByPaymentStatus(PaymentStatus.PENDING);
            List<Bill> paidBills = billDAO.getBillsByPaymentStatus(PaymentStatus.PAID);
            List<Bill> failedBills = billDAO.getBillsByPaymentStatus(PaymentStatus.FAILED);

            // Verify
            assertNotNull(pendingBills, "Pending bills list should not be null");
            assertEquals(1, pendingBills.size(), "There should be one pending bill");
            assertEquals(PaymentStatus.PENDING, pendingBills.get(0).getPaymentStatus(), "Bill should have PENDING status");

            assertNotNull(paidBills, "Paid bills list should not be null");
            assertEquals(1, paidBills.size(), "There should be one paid bill");
            assertEquals(PaymentStatus.PAID, paidBills.get(0).getPaymentStatus(), "Bill should have PAID status");

            assertNotNull(failedBills, "Failed bills list should not be null");
            assertEquals(1, failedBills.size(), "There should be one failed bill");
            assertEquals(PaymentStatus.FAILED, failedBills.get(0).getPaymentStatus(), "Bill should have FAILED status");
        } catch (SQLException e) {
            fail("Failed to get bills by payment status: " + e.getMessage());
        }
    }

    @Test
    void testGetBillByNumberNotFound() throws SQLException {
        // Try to get a non-existent bill
        Bill nonExistentBill = billDAO.getBillByNumber(9999);

        // Verify
        assertNull(nonExistentBill, "Non-existent bill should be null");
    }

    @Test
    void testUpdateNonExistentBill() throws SQLException {
        // Create a bill with a non-existent bill number
        Bill nonExistentBill = new Bill(
            9999,
            testBooking,
            100.0,
            10.0,
            5.0,
            95.0,
            LocalDateTime.now(),
            PaymentStatus.PENDING
        );

        // Try to update the non-existent bill
        boolean updated = billDAO.updateBill(nonExistentBill);

        // Verify
        assertTrue(!updated, "Updating a non-existent bill should return false");
    }

    @Test
    void testUpdateNonExistentPaymentStatus() throws SQLException {
        // Try to update payment status of a non-existent bill
        boolean updated = billDAO.updatePaymentStatus(9999, PaymentStatus.PAID);

        // Verify
        assertTrue(!updated, "Updating payment status of a non-existent bill should return false");
    }

    @Test
    void testDeleteNonExistentBill() throws SQLException {
        // Try to delete a non-existent bill
        boolean deleted = billDAO.deleteBill(9999);

        // Verify
        assertTrue(!deleted, "Deleting a non-existent bill should return false");
    }
}