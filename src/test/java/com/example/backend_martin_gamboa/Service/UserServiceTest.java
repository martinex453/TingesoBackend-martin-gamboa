package com.example.backend_martin_gamboa.Service;

import com.example.backend_martin_gamboa.Entity.LoanEntity;
import com.example.backend_martin_gamboa.Entity.UserEntity;
import com.example.backend_martin_gamboa.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        // Given
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        user = new UserEntity();
        user.setId(1L);
        user.setName("Martin Gamboa");
        user.setRut("12345678-9");
        LocalDate localDate = LocalDate.parse("2000-11-02");
        Date birthdate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setBirthdate(birthdate);
        user.setEmail("martin@gmail.com");
        user.setPassword("123456");
        user.setAddress("Casa 123");
        user.setPhone("123456789");
        user.setUsertype(1);
    }

    @Test
    void whenSaveUser_thenSuccess() {
        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByRut(user.getRut())).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);

        // Then
        assertThat(savedUser).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    void whenSaveExistingUser_thenReturnNull() {
        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserEntity savedUser = userService.saveUser(user);

        // Then
        assertThat(savedUser).isNull();
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.getUserById(user.getId());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenGetUserByEmail_thenReturnUser() {
        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserEntity foundUser = userService.getUserByEmail(user.getEmail());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenDeleteUser_thenReturnTrue() throws Exception {
        // When
        doNothing().when(userRepository).deleteById(user.getId());

        boolean result = userService.deleteUser(user.getId());

        // Then
        assertThat(result).isTrue();
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnUserId() {
        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Long userId = userService.login(user.getEmail(), user.getPassword());

        // Then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    void whenLoginWithInvalidEmail_thenReturnZero() {
        // When
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);

        Long userId = userService.login("invalid@example.com", user.getPassword());

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenLoginWithInvalidPassword_thenReturnZero() {
        // When
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        Long userId = userService.login(user.getEmail(), "wrongpassword");

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenUserAgeIsRequested_thenReturnCorrectAge() {
        // When
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Integer age = userService.userAge(user.getId());

        // Then
        assertThat(age).isEqualTo(24);
    }

    @Test
    void whenAgeLimitIsChecked_thenReturnTrueForValidAge() {
        // Given
        Integer age = 30;

        // When
        Boolean result = userService.ageLimit(age);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void whenAgeLimitIsChecked_thenReturnFalseForUnderage() {
        // Given
        Integer age = 15;

        // When
        Boolean result = userService.ageLimit(age);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void whenAgeLimitIsChecked_thenReturnFalseForOverage() {
        // Given
        Integer age = 75;

        // When
        Boolean result = userService.ageLimit(age);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void whenGetUserByPhone_thenReturnUser() {
        // When
        when(userRepository.findByPhone(user.getPhone())).thenReturn(user);

        UserEntity foundUser = userService.getUserByPhone(user.getPhone());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenGetUserByName_thenReturnListOfUsers() {
        // Given
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        // When
        when(userRepository.findByName(user.getName())).thenReturn(users);

        List<UserEntity> foundUsers = userService.getUserByName(user.getName());

        // Then
        assertThat(foundUsers).containsExactly(user);
    }

    @Test
    void whenGetAllUsers_thenReturnListOfUsers() {
        // Given
        List<UserEntity> users = new ArrayList<>();
        users.add(user);

        // When
        when(userRepository.findAll()).thenReturn(users);
        List<UserEntity> foundUsers = userService.getAllUsers();

        // Then
        assertThat(foundUsers).containsExactly(user);
    }

    @Test
    void whenUpdateUser_thenSuccess() {
        // Given
        user.setName("Martin Updated");

        // When
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity updatedUser = userService.updateUser(user);

        // Then
        assertThat(updatedUser.getName()).isEqualTo("Martin Updated");
        verify(userRepository).save(user);
    }

    @Test
    void whenUpdateNullUser_thenReturnNull() {
        // When
        UserEntity updatedUser = userService.updateUser(null);

        // Then
        assertThat(updatedUser).isNull();
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void whenDeleteNonExistentUser_thenThrowException() {
        // When
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(user.getId());

        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(user.getId());
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo("User not found");
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void whenLoginWithNullEmail_thenReturnZero() {
        // Given
        String email = null;
        String password = user.getPassword();

        // When
        Long userId = userService.login(email, password);

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenLoginWithNullPassword_thenReturnZero() {
        //Given
        String email = user.getEmail();
        String password = null;

        // When
        when(userRepository.findByEmail(email)).thenReturn(user);

        Long userId = userService.login(email, password);

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenAgeLimitIsCheckedWithNullAge_thenReturnFalse() {
        // Given
        Integer age = null;

        // When
        Boolean result = userService.ageLimit(age);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void whenUserNull_thenReturnNull() {
        // Given
        UserEntity user = null;

        // When
        UserEntity foundUser = userService.saveUser(user);

        // Then
        assertThat(foundUser).isNull();
    }

    @Test
    void whenUserWithSameEmailOrRutExists_thenReturnNull() {
        // Given
        UserEntity newUser = new UserEntity();
        newUser.setEmail("existing@example.com");
        newUser.setRut("12345678-9");
        UserEntity existingUserByEmail = new UserEntity();
        existingUserByEmail.setEmail("existing@example.com");

        // When
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(existingUserByEmail);
        when(userRepository.findByRut(newUser.getRut())).thenReturn(null);
        UserEntity result = userService.saveUser(newUser);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void whenUserWithSameRutExists_thenReturnNull() {
        // Given
        UserEntity newUser = new UserEntity();
        newUser.setEmail("newuser@example.com");
        newUser.setRut("existingRut");
        UserEntity existingUserByRut = new UserEntity();
        existingUserByRut.setRut("existingRut");

        // When
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(userRepository.findByRut(newUser.getRut())).thenReturn(existingUserByRut);
        UserEntity result = userService.saveUser(newUser);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void whenUserExistsButBirthdayNotYetThisYear_thenReturnCorrectAge() {
        // Given
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setBirthdate(Date.from(LocalDate.of(2000, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        //When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Integer age = userService.userAge(userId);

        LocalDate currentLocalDate = LocalDate.now();
        Integer expectedAge = currentLocalDate.getYear() - 2000 - 1; // Ajustar por el cumpleaños no pasado

        // Then
        assertEquals(expectedAge, age);
    }

    @Test
    void whenUserExistsAndBirthdayHasPassedThisYear_thenReturnCorrectAge() {
        // Given
        Long userId = 2L;
        UserEntity user = new UserEntity();
        user.setBirthdate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Integer age = userService.userAge(userId);

        LocalDate currentLocalDate = LocalDate.now();
        Integer expectedAge = currentLocalDate.getYear() - 2000; // No se necesita ajuste

        // Then
        assertEquals(expectedAge, age);
    }

    @Test
    void  whenUserNull_thenReturnAgeZero(){
        // Given
        Long userId = 1L;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Integer age = userService.userAge(userId);

        //Then
        assertThat(age).isEqualTo(0);
    }
}
