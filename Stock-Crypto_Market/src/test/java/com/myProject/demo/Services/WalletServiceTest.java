package com.myProject.demo.Services;

import com.myProject.demo.DTO.WalletRequest;
import com.myProject.demo.DTO.WalletResponse;
import com.myProject.demo.Exceptions.UserNotFoundException;
import com.myProject.demo.Models.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.myProject.demo.Models.Wallet;
import com.myProject.demo.Repositories.UserRepo;
import com.myProject.demo.Repositories.WalletRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    UserRepo userrepo;
    @Mock
    WalletRepo walletrepo;
    @Mock
    ModelMapper modelmapper;
    @InjectMocks
    WalletService walletservice;

    private WalletRequest walletrequest;
    private User user;
    private Wallet wallet;


    @BeforeEach
    public void setup(){
        walletrequest = new WalletRequest(
                1L,
                new BigDecimal(1000),
                "USD"
        );
        user = new User();
        user.setId(1L);
        user.setUsername("BARAA");

        wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100.00"));
        wallet.setCurrency("USD");
    }

    @Test
    public void Addwalletwhenuserexixts()
    {
        when(userrepo.findById(1L)).thenReturn(Optional.of(user));
        when(modelmapper.map(walletrequest, Wallet.class)).thenReturn(wallet);

        walletservice.AddWallet(walletrequest);


        assertEquals(user, wallet.getUser());

        verify(userrepo).findById(1L);
        verify(modelmapper).map(walletrequest, Wallet.class);
        verify(walletrepo).save(wallet);

    }

    @Test
    public void addWalletsouldThrowUsernotfoundExceptionwhenusernotfound()
    {
         walletrequest.setUser_id(99L);
         walletrequest.setBalance(new BigDecimal("100.00"));
         walletrequest.setCurrency("USD");
        when(userrepo.findById(99L)).thenReturn(Optional.empty());
        UserNotFoundException exception =assertThrows(UserNotFoundException.class,
                ()->walletservice.AddWallet(walletrequest));
        assertEquals("User not found", exception.getMessage());
        verify(userrepo).findById(99L);
        verify(modelmapper, never()).map(any(), eq(Wallet.class));
        verify(walletrepo, never()).save(any());

    }


    @Test
    public void updateWalletShouldUpdatedSuccessfully() {


        walletrequest.setBalance(new BigDecimal("10000.00"));
        walletrequest.setCurrency("POD");

        WalletResponse expectedResponse = new WalletResponse();
        expectedResponse.setBalance(new BigDecimal("10000.00"));
        expectedResponse.setCurrency("POD");

        Mockito.when(walletrepo.findById(1L)).thenReturn(Optional.of(wallet));
        Mockito.when(userrepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(walletrepo.save(any(Wallet.class))).thenReturn(wallet);



        Mockito.when(modelmapper.map(wallet, WalletResponse.class))
                .thenReturn(expectedResponse);


        WalletResponse result = walletservice.updateWallet(walletrequest, 1L);


        assertNotNull(result);
        assertEquals(new BigDecimal("10000.00"), result.getBalance());
        assertEquals("POD", result.getCurrency());


        verify(walletrepo).findById(1L);
        verify(walletrepo).save(wallet);
        verify(modelmapper).map(wallet, WalletResponse.class);
    }

    @Test
    void getAllWalletsShouldReturnList() {


        WalletResponse w1 = new WalletResponse();
        w1.setBalance(new BigDecimal("1000"));

        WalletResponse w2 = new WalletResponse();
        w2.setBalance(new BigDecimal("2000"));

        List<WalletResponse> list = List.of(w1, w2);

        Mockito.when(walletrepo.findAllWallets())
                .thenReturn(list);

        List<WalletResponse> result = walletservice.getallWallets();


        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(new BigDecimal("1000"), result.get(0).getBalance());

        Mockito.verify(walletrepo).findAllWallets();
    }


}
