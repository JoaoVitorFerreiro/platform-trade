package com.plataformtrade.domain;

import com.plataformtrade.domain.VOs.Document;
import com.plataformtrade.domain.VOs.Email;
import com.plataformtrade.domain.VOs.Name;
import com.plataformtrade.domain.VOs.Password;
import com.plataformtrade.domain.repositories.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Domain Tests")
class AccountTest {

    private static final String VALID_CPF_1 = "12345678909";
    private static final String VALID_CPF_2 = "11144477735";
    private static final String VALID_CPF_3 = "52998224725";
    private static final PasswordHasher PASSWORD_HASHER = new PasswordHasher() {
        @Override
        public String hash(String password) {
            return "hashed-" + password;
        }

        @Override
        public boolean matches(String password, String hash) {
            return hash.equals(hash(password));
        }
    };

    @Test
    @DisplayName("Should create a new account with generated ID")
    void shouldCreateNewAccountWithGeneratedId() {
        Account account = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals(36, account.getAccountId().length());
        assertEquals("Joao Silva", account.getName().getValue());
        assertEquals(VALID_CPF_1, account.getDocument().getValue());
        assertEquals("hashed-Senha123", account.getPassword().getValue());
        assertEquals("joao@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should restore an existing account with provided ID")
    void shouldRestoreExistingAccountWithProvidedId() {
        String accountId = "123e4567-e89b-12d3-a456-426614174000";

        Account account = Account.restore(
                accountId,
                "Maria Santos",
                VALID_CPF_2,
                "hashed-Password1",
                "maria@email.com"
        );

        assertNotNull(account);
        assertEquals(accountId, account.getAccountId());
        assertEquals("Maria Santos", account.getName().getValue());
        assertEquals(VALID_CPF_2, account.getDocument().getValue());
        assertEquals("hashed-Password1", account.getPassword().getValue());
        assertEquals("maria@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should create account using constructor")
    void shouldCreateAccountUsingConstructor() {
        Account account = new Account(
                "testid",
                "Pedro Oliveira",
                VALID_CPF_3,
                "Test1234",
                "pedro@email.com"
        );

        assertEquals("testid", account.getAccountId());
        assertEquals("Pedro Oliveira", account.getName().getValue());
    }

    @Test
    @DisplayName("Should update name")
    void shouldUpdateName() {
        Account account = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        account.setName("Joao Pedro");

        assertEquals("Joao Pedro", account.getName().getValue());
    }

    @Test
    @DisplayName("Should update email")
    void shouldUpdateEmail() {
        Account account = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        account.setEmail(new Email("novo@email.com"));

        assertEquals("novo@email.com", account.getEmail().getValue());
    }

    @Test
    @DisplayName("Should update password")
    void shouldUpdatePassword() {
        Account account = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        account.setPassword(new Password("NovaSenha123"));

        assertEquals("NovaSenha123", account.getPassword().getValue());
    }

    @Test
    @DisplayName("Should update document")
    void shouldUpdateDocument() {
        Account account = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        account.setDocument(new Document(VALID_CPF_2));

        assertEquals(VALID_CPF_2, account.getDocument().getValue());
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid name")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "J",
                    VALID_CPF_1,
                    "Senha123",
                    "joao@email.com",
                    PASSWORD_HASHER
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid document")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidDocument() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "Joao Silva",
                    "99999999999",
                    "Senha123",
                    "joao@email.com",
                    PASSWORD_HASHER
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid password")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "Joao Silva",
                    VALID_CPF_1,
                    "123",
                    "joao@email.com",
                    PASSWORD_HASHER
            );
        });
    }

    @Test
    @DisplayName("Should throw exception when creating account with invalid email")
    void shouldThrowExceptionWhenCreatingAccountWithInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account.create(
                    "Joao Silva",
                    VALID_CPF_1,
                    "Senha123",
                    "email-invalido",
                    PASSWORD_HASHER
            );
        });
    }

    @Test
    @DisplayName("Should generate different IDs for different accounts")
    void shouldGenerateDifferentIdsForDifferentAccounts() {
        Account account1 = Account.create(
                "Joao Silva",
                VALID_CPF_1,
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        Account account2 = Account.create(
                "Maria Santos",
                VALID_CPF_2,
                "Password1",
                "maria@email.com",
                PASSWORD_HASHER
        );

        assertNotEquals(account1.getAccountId(), account2.getAccountId());
    }

    @Test
    @DisplayName("Account ID should be immutable")
    void accountIdShouldBeImmutable() {
        String originalId = "fixedid";
        Account account = Account.restore(
                originalId,
                "Joao Silva",
                VALID_CPF_1,
                "hashed-Senha123",
                "joao@email.com"
        );

        assertEquals(originalId, account.getAccountId());
    }

    @Test
    @DisplayName("Should accept CPF with formatting and store without it")
    void shouldAcceptCpfWithFormattingAndStoreWithoutIt() {
        Account account = Account.create(
                "Joao Silva",
                "123.456.789-09",
                "Senha123",
                "joao@email.com",
                PASSWORD_HASHER
        );

        assertEquals("12345678909", account.getDocument().getValue());
    }
}
