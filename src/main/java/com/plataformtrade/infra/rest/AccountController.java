package com.plataformtrade.infra.rest;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.application.dtos.common.ApiResponse;
import com.plataformtrade.application.dtos.common.PageResponse;
import com.plataformtrade.application.usecases.GetAccount;
import com.plataformtrade.application.usecases.GetAllAccounts;
import com.plataformtrade.application.usecases.Signup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Account REST Controller
 *
 * This controller delegates to specific use cases following Clean Architecture principles.
 * Each endpoint is mapped to a single use case with a clear business intention.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "API de gerenciamento de contas")
public class AccountController {
    private final Signup signupUseCase;
    private final GetAccount getAccount;
    private final GetAllAccounts getAllAccountsUseCase;

    public AccountController(
            Signup signupUseCase,
            GetAccount getAccount,
            GetAllAccounts getAllAccountsUseCase
    ) {
        this.signupUseCase = signupUseCase;
        this.getAccount = getAccount;
        this.getAllAccountsUseCase = getAllAccountsUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new account (Signup)")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@RequestBody CreateAccountRequest request) {
        AccountResponse account = signupUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(account));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by id")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountId) {
        AccountResponse account = getAccount.execute(accountId);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @GetMapping
    @Operation(summary = "List all accounts")
    public ResponseEntity<ApiResponse<PageResponse<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> accounts = getAllAccountsUseCase.execute();
        PageResponse<AccountResponse> pageResponse = PageResponse.of(accounts);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
}