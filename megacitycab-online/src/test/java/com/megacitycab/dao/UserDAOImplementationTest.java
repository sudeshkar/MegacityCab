package com.megacitycab.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.megacitycab.model.User;
import com.megacitycab.model.UserRole;

class UserDAOImplementationTest {

    private UserDAOImplementation userDAO;
    private User testUser;

    @BeforeEach
    void setUp() {
        try {

            userDAO = new UserDAOImplementation();

            // Setup test user
            testUser = new User();
            testUser.setName("testUser");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123");
            testUser.setRole(UserRole.CUSTOMER);
            testUser.setLastLogindate(Timestamp.valueOf(LocalDateTime.now()));

        } catch (Exception e) {
            fail("Failed to setup test: " + e.getMessage());
        }
    }

    @Test
    void testAddUser() {
        // Test adding a user
        boolean result = userDAO.addUser(testUser);
        assertTrue(result, "User should be added successfully");

        // Verify user was added
        User retrieved = userDAO.getUserByEmail(testUser.getEmail());
        assertNotNull(retrieved, "User should be retrievable after adding");
        assertEquals(testUser.getName(), retrieved.getName(), "Names should match");

        // Cleanup
        userDAO.deleteUser(retrieved.getUserID());
    }

    @Test
    void testGetUserById() {
        // First add a user
        int userId = userDAO.createUser(testUser);
        assertTrue(userId > 0, "User creation should return valid ID");

        // Test getting user by ID
        User retrieved = userDAO.getUserById(userId);
        assertNotNull(retrieved, "User should be found by ID");
        assertEquals(userId, retrieved.getUserID(), "User IDs should match");
        assertEquals(testUser.getEmail(), retrieved.getEmail(), "Emails should match");

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    void testGetUserByEmail() {
        // Add user first
        userDAO.addUser(testUser);

        // Test getting user by email
        User retrieved = userDAO.getUserByEmail(testUser.getEmail());
        assertNotNull(retrieved, "User should be found by email");
        assertEquals(testUser.getName(), retrieved.getName(), "Names should match");

        // Cleanup
        userDAO.deleteUser(retrieved.getUserID());
    }

    @Test
    void testGetAllUsers() {
        // Add at least one user
        userDAO.addUser(testUser);

        // Test getting all users
        java.util.List<User> users = userDAO.getAllUsers();
        assertFalse(users.isEmpty(), "User list should not be empty");

        // Verify our test user is in the list
        boolean found = users.stream()
            .anyMatch(u -> u.getEmail().equals(testUser.getEmail()));
        assertTrue(found, "Test user should be in the list");

        // Cleanup
        users.forEach(u -> userDAO.deleteUser(u.getUserID()));
    }

    @Test
    void testUpdateUser() {
        // Add user first
        int userId = userDAO.createUser(testUser);

        // Modify user
        testUser.setUserID(userId);
        testUser.setName("updatedUser");
        boolean result = userDAO.updateUser(testUser);
        assertTrue(result, "Update should be successful");

        // Verify update
        User updated = userDAO.getUserById(userId);
        assertEquals("updatedUser", updated.getName(), "Name should be updated");

        // Cleanup
        userDAO.deleteUser(userId);
    }

    @Test
    void testDeleteUser() {
        // Add user first
        int userId = userDAO.createUser(testUser);

        // Test deletion
        boolean result = userDAO.deleteUser(userId);
        assertTrue(result, "Deletion should be successful");

        // Verify deletion
        User deleted = userDAO.getUserById(userId);
        assertNull(deleted, "User should not be found after deletion");
    }

    @Test
    void testLogin() {
        // Add user first
        userDAO.addUser(testUser);

        // Test login
        User loggedIn = userDAO.login(testUser.getEmail(), "password123");
        assertNotNull(loggedIn, "Login should succeed with correct credentials");
        assertEquals(testUser.getEmail(), loggedIn.getEmail(), "Emails should match");

        // Test wrong password
        User failedLogin = userDAO.login(testUser.getEmail(), "wrongpassword");
        assertNull(failedLogin, "Login should fail with wrong password");

        // Cleanup
        userDAO.deleteUser(loggedIn.getUserID());
    }

    @Test
    void testCreateUser() {
        // Test creating user
        int userId = userDAO.createUser(testUser);
        assertTrue(userId > 0, "Created user should have positive ID");

        // Verify user exists
        User created = userDAO.getUserById(userId);
        assertNotNull(created, "Created user should be retrievable");
        assertEquals(testUser.getEmail(), created.getEmail(), "Emails should match");

        // Cleanup
        userDAO.deleteUser(userId);
    }
}