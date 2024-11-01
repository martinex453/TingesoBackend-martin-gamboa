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
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.findByRut(user.getRut())).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        // When
        UserEntity savedUser = userService.saveUser(user);

        // Then
        assertThat(savedUser).isEqualTo(user);
        verify(userRepository).save(user);
    }

    @Test
    void whenSaveExistingUser_thenReturnNull() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        UserEntity savedUser = userService.saveUser(user);

        // Then
        assertThat(savedUser).isNull();
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void whenGetUserById_thenReturnUser() {
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        UserEntity foundUser = userService.getUserById(user.getId());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenGetUserByEmail_thenReturnUser() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        UserEntity foundUser = userService.getUserByEmail(user.getEmail());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenDeleteUser_thenReturnTrue() throws Exception {
        // Given
        doNothing().when(userRepository).deleteById(user.getId());

        // When
        boolean result = userService.deleteUser(user.getId());

        // Then
        assertThat(result).isTrue();
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnUserId() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        Long userId = userService.login(user.getEmail(), user.getPassword());

        // Then
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    void whenLoginWithInvalidEmail_thenReturnZero() {
        // Given
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(null);

        // When
        Long userId = userService.login("invalid@example.com", user.getPassword());

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenLoginWithInvalidPassword_thenReturnZero() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        Long userId = userService.login(user.getEmail(), "wrongpassword");

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenUserAgeIsRequested_thenReturnCorrectAge() {
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        Integer age = userService.userAge(user.getId());

        // Then
        assertThat(age).isEqualTo(23); // Dependiendo de la fecha actual, ajustar si es necesario
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
        // Given
        when(userRepository.findByPhone(user.getPhone())).thenReturn(user);

        // When
        UserEntity foundUser = userService.getUserByPhone(user.getPhone());

        // Then
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void whenGetUserByName_thenReturnListOfUsers() {
        // Given
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findByName(user.getName())).thenReturn(users);

        // When
        List<UserEntity> foundUsers = userService.getUserByName(user.getName());

        // Then
        assertThat(foundUsers).containsExactly(user);
    }

    @Test
    void whenGetAllUsers_thenReturnListOfUsers() {
        // Given
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserEntity> foundUsers = userService.getAllUsers();

        // Then
        assertThat(foundUsers).containsExactly(user);
    }

    @Test
    void whenUpdateUser_thenSuccess() {
        // Given
        user.setName("Martin Updated");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        // When
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
        // Given
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(user.getId());

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            userService.deleteUser(user.getId());
        });

        // Then
        assertThat(exception.getMessage()).isEqualTo("User not found");
        verify(userRepository).deleteById(user.getId());
    }

    @Test
    void whenLoginWithNullEmail_thenReturnZero() {
        // When
        Long userId = userService.login(null, user.getPassword());

        // Then
        assertThat(userId).isEqualTo(0L);
    }

    @Test
    void whenLoginWithNullPassword_thenReturnZero() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // When
        Long userId = userService.login(user.getEmail(), null);

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
        // Dado
        UserEntity newUser = new UserEntity();
        newUser.setEmail("existing@example.com");
        newUser.setRut("12345678-9");

        // Simular que ya existe un usuario con el mismo email
        UserEntity existingUserByEmail = new UserEntity();
        existingUserByEmail.setEmail("existing@example.com");
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(existingUserByEmail);

        // Simular que no existe un usuario con el mismo rut
        when(userRepository.findByRut(newUser.getRut())).thenReturn(null);

        // Cuando
        UserEntity result = userService.saveUser(newUser);

        // Entonces
        assertThat(result).isNull(); // Verifica que el resultado sea null
    }

    @Test
    void whenUserWithSameRutExists_thenReturnNull() {
        // Dado
        UserEntity newUser = new UserEntity();
        newUser.setEmail("newuser@example.com");
        newUser.setRut("existingRut"); // Suponiendo que este RUT ya existe

        // Simular que no existe un usuario con el mismo email
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);

        // Simular que ya existe un usuario con el mismo RUT
        UserEntity existingUserByRut = new UserEntity();
        existingUserByRut.setRut("existingRut");
        when(userRepository.findByRut(newUser.getRut())).thenReturn(existingUserByRut);

        // Cuando
        UserEntity result = userService.saveUser(newUser);

        // Entonces
        assertThat(result).isNull(); // Verifica que el resultado sea null
    }

    /*@Test
    void whenUserNotFound_thenReturnZero() {
        // Simular que no se encuentra el usuario
        Long userId = 1L;
        UserEntity mockUser = null; // Simulamos que no hay usuario

        // Configurar el mock para que devuelva null
        when(userRepository.findById(userId)).thenReturn(mockUser); // Retorna UserEntity como null

        // Ejecutar el método
        Integer age = userService.userAge(userId);

        // Verificar que se devuelve 0
        assertEquals(0, age);
    }*/

    @Test
    void whenUserExistsButBirthdayNotYetThisYear_thenReturnCorrectAge() {
        // Simular que se encuentra el usuario
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setBirthdate(Date.from(LocalDate.of(2000, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Ejecutar el método
        Integer age = userService.userAge(userId);

        // Obtener la fecha actual para calcular la edad
        LocalDate currentLocalDate = LocalDate.now();
        Integer expectedAge = currentLocalDate.getYear() - 2000 - 1; // Ajustar por el cumpleaños no pasado

        // Verificar que la edad devuelta sea la esperada
        assertEquals(expectedAge, age);
    }

    @Test
    void whenUserExistsAndBirthdayHasPassedThisYear_thenReturnCorrectAge() {
        // Simular que se encuentra el usuario
        Long userId = 2L;
        UserEntity user = new UserEntity();
        user.setBirthdate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Ejecutar el método
        Integer age = userService.userAge(userId);

        // Obtener la fecha actual para calcular la edad
        LocalDate currentLocalDate = LocalDate.now();
        Integer expectedAge = currentLocalDate.getYear() - 2000; // No se necesita ajuste

        // Verificar que la edad devuelta sea la esperada
        assertEquals(expectedAge, age);
    }

    @Test
    void  whenUserNull_thenReturnAgeZero(){
        // Given
        Long userId = 1L;

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Integer age = userService.userAge(userId);
        assertThat(age).isEqualTo(0);
    }
}
