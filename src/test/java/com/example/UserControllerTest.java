package com.example;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.controller.UserController;
import com.example.entity.User;
import com.example.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById_ExistingUser_ReturnsUser() {
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe", "john@example.com", "xyz", "xyz");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetUserById_NonExistingUser_ReturnsNotFound() {
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(null);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }


    @Test
    public void testGetAllUsers_ReturnsListOfUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "John Doe", "john@example.com","xyz", "xyz"),
                new User(2L, "Jane Doe", "jane@example.com","xyz", "xyz")
        );
        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testCreateUser_ValidUser_ReturnsCreatedUser() {
        User newUser = new User(null, "Alice Smith", "alice@example.com","xyz", "xyz");
        User createdUser = new User(3L, "Alice Smith", "alice@example.com","xyz", "xyz");
        when(userService.createUser(newUser)).thenReturn(createdUser);

        ResponseEntity<User> response = userController.createUser(newUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).createUser(newUser);
    }
}