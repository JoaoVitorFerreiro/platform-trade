package com.plataformtrade.infra.rest;

import com.plataformtrade.application.dtos.AccountResponse;
import com.plataformtrade.application.dtos.CreateAccountRequest;
import com.plataformtrade.application.dtos.common.ApiResponse;
import com.plataformtrade.application.dtos.common.PageResponse;
import com.plataformtrade.application.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "API de gerenciamento de contas")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@RequestBody CreateAccountRequest request) {
        AccountResponse account = accountService.createAccount(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(account));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by id")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountId) {
        AccountResponse account = accountService.getAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @GetMapping
    @Operation(summary = "List all accounts")
    public ResponseEntity<ApiResponse<PageResponse<AccountResponse>>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts();
        PageResponse<AccountResponse> pageResponse = PageResponse.of(accounts);
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
}