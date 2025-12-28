package com.plataformtrade.domain;

import com.plataformtrade.domain.VOs.Document;
import com.plataformtrade.domain.VOs.Email;
import com.plataformtrade.domain.VOs.Name;
import com.plataformtrade.domain.VOs.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Domain Tests")
class AccountTest {

    private static final String VALID_CPF_1 = "12345678909";
    private static final String VALID_CPF_2 = "11144477735";
    private static final String VALID_CPF_3 = "52998224725";

    @Test
    @DisplayName("Should create a new account with generated ID")
    void shouldCreateNewAccountWithGeneratedId() {
        Account account = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals(36, account.getAccountId().length());
        assertEquals("JoaoSilva", account.getName().getValue());
        assertEquals(VALID_CPF_1, account.getDocument().getValue());
        assertEquals("Senha123", account.getPassword().getValue());
        assertEquals("joao@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should restore an existing account with provided ID")
    void shouldRestoreExistingAccountWithProvidedId() {
        String accountId = "123e4567-e89b-12d3-a456-426614174000";

        Account account = Account.restore(
                accountId,
                "MariaSantos",
                VALID_CPF_2,
                "Password1",
                "maria@email.com"
        );

        assertNotNull(account);
        assertEquals(accountId, account.getAccountId());
        assertEquals("MariaSantos", account.getName().getValue());
        assertEquals(VALID_CPF_2, account.getDocument().getValue());
        assertEquals("Password1", account.getPassword().getValue());
        assertEquals("maria@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should create account using constructor")
    void shouldCreateAccountUsingConstructor() {
        Account account = new Account(
                "testid",
                "PedroOliveira",
                VALID_CPF_3,
                "Test1234",
                "pedro@email.com"
        );

        assertEquals("testid", account.getAccountId());
        assertEquals("PedroOliveira", account.getName().getValue());
    }

    @Test
    @DisplayName("Should update name")
    void shouldUpdateName() {
        Account account = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        account.setName("JoaoPedro");

        assertEquals("JoaoPedro", account.getName().getValue());
    }

    @Test
    @DisplayName("Should update email")
    void shouldUpdateEmail() {
        Account account = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        account.setEmail(new Email("novo@email.com"));

        assertEquals("novo@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should update password")
    void shouldUpdatePassword() {
        Account account = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        account.setPassword(new Password("NovaSenha123"));

        assertEquals("NovaSenha123", account.getPassword().getValue());
    }

    @Test
    @DisplayName("Should update document")
    void shouldUpdateDocument() {
        Account account = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        account.setDocument(new Document(VALID_CPF_2));

        assertEquals(VALID_CPF_2, account.getDocument().getValue());
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid name")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "João Silva",
                    VALID_CPF_1,
                    "Senha123",
                    "joao@email.com"
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid document")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidDocument() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "JoaoSilva",
                    "99999999999",
                    "Senha123",
                    "joao@email.com"
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid password")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "JoaoSilva",
                    VALID_CPF_1,
                    "123",
                    "joao@email.com"
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid email")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "JoaoSilva",
                    VALID_CPF_1,
                    "Senha123",
                    "email-invalido"
            );
        });
    }

    @Test
    @DisplayName("Should generate different IDs for different accounts")
    void shouldGenerateDifferentIdsForDifferentAccounts() {
        Account account1 = Account.create(
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        Account account2 = Account.create(
                "MariaSantos",
                VALID_CPF_2,
                "Password1",
                "maria@email.com"
        );

        assertNotEquals(account1.getAccountId(), account2.getAccountId());
    }

    @Test
    @DisplayName("Account ID should be immutable")
    void accountIdShouldBeImmutable() {
        String originalId = "fixedid";
        Account account = Account.restore(
                originalId,
                "JoaoSilva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com"
        );

        assertEquals(originalId, account.getAccountId());
    }

    @Test
    @DisplayName("Should accept CPF with formatting and store without it")
    void shouldAcceptCpfWithFormattingAndStoreWithoutIt() {
        Account account = Account.create(
                "JoaoSilva",
                "123.456.789-09",
                "Senha123",
                "joao@email.com"
        );

        assertEquals("12345678909", account.getDocument().getValue());
    }
}