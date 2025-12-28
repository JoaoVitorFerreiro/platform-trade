# 🚀 Roadmap de Implementação - Platform Trade

Baseado na análise comparativa com o repositório [cccat22_8](https://github.com/rodrigobranas/cccat22_8).

---

## 📋 Visão Geral

Este roadmap guia a evolução do projeto de uma aplicação básica CRUD para uma **plataforma de trading event-driven completa**.

**Total de tarefas: 62**
**Tempo estimado: 8-12 semanas** (trabalhando meio período)

---

## 🎯 FASE 1: Fundação - Estrutura de Use Cases

**Objetivo**: Refatorar de Services genéricos para Use Cases específicos (padrão Clean Architecture)

### Tarefas:
1. ✅ Criar estrutura `application/usecases/`
2. ✅ Implementar `SignupUseCase` (extrair lógica do `AccountService`)
3. ✅ Implementar `GetAccountUseCase`

### Estrutura esperada:
```
src/main/java/com/plataformtrade/application/
├── usecases/
│   ├── account/
│   │   ├── SignupUseCase.java
│   │   └── GetAccountUseCase.java
│   └── ...
└── dtos/
```

### Benefícios:
- Responsabilidade única
- Fácil testar
- Reutilizável
- Segue princípios SOLID

---

## 🏗️ FASE 2: Entidades de Trading - Domain Layer

**Objetivo**: Criar modelo de domínio rico para plataforma de trading

### Tarefas:
4. ✅ Criar entidade `Order` com VOs (`OrderId`, `Price`, `Quantity`, `OrderType`, `OrderStatus`)
5. ✅ Criar agregado `Wallet` com `Balance`
6. ✅ Adicionar `Wallet` ao `Account`
7. ✅ Implementar métodos `deposit()` e `withdraw()` no `Account`

### Estrutura esperada:
```
domain/
├── Account.java (agregado raiz)
├── Order.java (entidade)
├── Wallet.java (agregado)
├── Balance.java (entidade de valor)
└── VOs/
    ├── OrderId.java
    ├── Price.java
    ├── Quantity.java
    ├── OrderType.java (LIMIT, MARKET)
    └── OrderStatus.java (PENDING, FILLED, CANCELLED)
```

### Exemplo de código:
```java
public class Account {
    private Wallet wallet;
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public void deposit(String assetId, BigDecimal amount) {
        wallet.deposit(assetId, amount);
        domainEvents.add(new DepositMadeEvent(this.accountId, assetId, amount));
    }

    public void withdraw(String assetId, BigDecimal amount) {
        wallet.withdraw(assetId, amount);
        domainEvents.add(new WithdrawMadeEvent(this.accountId, assetId, amount));
    }
}
```

---

## 📡 FASE 3: Eventos de Domínio

**Objetivo**: Implementar Domain Events para capturar mudanças importantes

### Tarefas:
8. ✅ Criar classe base `DomainEvent`
9. ✅ Criar `AccountCreatedEvent`
10. ✅ Criar `DepositMadeEvent` e `WithdrawMadeEvent`
11. ✅ Adicionar `List<DomainEvent> domainEvents` no `Account`

### Estrutura esperada:
```
domain/events/
├── DomainEvent.java (base class)
├── AccountCreatedEvent.java
├── DepositMadeEvent.java
├── WithdrawMadeEvent.java
├── OrderPlacedEvent.java
└── OrderFilledEvent.java
```

### Exemplo de código:
```java
public abstract class DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final LocalDateTime occurredOn = LocalDateTime.now();

    public abstract String getEventType();
}

public class AccountCreatedEvent extends DomainEvent {
    private final String accountId;
    private final String email;

    @Override
    public String getEventType() {
        return "AccountCreated";
    }
}
```

---

## 🐰 FASE 4: Mensageria - RabbitMQ Setup

**Objetivo**: Configurar infraestrutura de mensageria assíncrona

### Tarefas:
12. ✅ Adicionar `spring-boot-starter-amqp` no `pom.xml`
13. ✅ Criar `RabbitMQConfig` com exchanges e queues
14. ✅ Configurar Docker Compose com RabbitMQ
15. ✅ Criar interface `Queue` (port - hexagonal)
16. ✅ Implementar `RabbitMQAdapter` (adapter)

### Dependência:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### Configuração:
```java
@Configuration
public class RabbitMQConfig {

    public static final String ACCOUNT_EXCHANGE = "account.events";
    public static final String ACCOUNT_CREATED_QUEUE = "account.created";
    public static final String DEPOSIT_MADE_QUEUE = "deposit.made";

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(ACCOUNT_EXCHANGE, true, false);
    }

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(ACCOUNT_CREATED_QUEUE, true);
    }

    @Bean
    public Binding accountCreatedBinding() {
        return BindingBuilder
            .bind(accountCreatedQueue())
            .to(accountExchange())
            .with("account.created");
    }
}
```

### Docker Compose:
```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
```

---

## 📦 FASE 5: Outbox Pattern

**Objetivo**: Garantir consistência eventual entre banco de dados e mensageria

### Tarefas:
17. ✅ Criar migração Flyway para tabela `outbox_message`
18. ✅ Criar entidade `OutboxMessage`
19. ✅ Criar `OutboxRepository`
20. ✅ Criar `OutboxService` para salvar eventos
21. ✅ Criar `OutboxProcessor` com `@Scheduled` para processar mensagens

### Migração SQL:
```sql
-- V4__create_outbox_table.sql
CREATE TABLE outbox_message (
    message_id UUID PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    payload JSONB NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP
);

CREATE INDEX idx_outbox_status ON outbox_message(status);
```

### Entidade:
```java
@Entity
@Table(name = "outbox_message")
public class OutboxMessage {
    @Id
    private String messageId;
    private String eventType;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, SENT, FAILED

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
```

### Processor:
```java
@Service
public class OutboxProcessor {

    @Scheduled(fixedDelay = 500) // 500ms
    public void processMessages() {
        List<OutboxMessage> pending = outboxRepository
            .findByStatus(OutboxStatus.PENDING);

        for (OutboxMessage message : pending) {
            try {
                queueAdapter.publish(message.getEventType(), message.getPayload());
                message.setStatus(OutboxStatus.SENT);
                message.setSentAt(LocalDateTime.now());
                outboxRepository.save(message);
            } catch (Exception e) {
                message.setStatus(OutboxStatus.FAILED);
                outboxRepository.save(message);
            }
        }
    }
}
```

---

## 📢 FASE 6: Event Publishing

**Objetivo**: Publicar eventos de domínio no RabbitMQ

### Tarefas:
22. ✅ Criar `DomainEventPublisher`
23. ✅ Integrar publicação de eventos no `AccountService`
24. ✅ Criar `@TransactionalEventListener` para salvar no outbox

### Implementação:
```java
@Service
public class DomainEventPublisher {

    private final OutboxService outboxService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDomainEvent(DomainEvent event) {
        outboxService.save(event);
    }

    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
```

---

## 🎧 FASE 7: Event Consumers/Handlers

**Objetivo**: Consumir e processar eventos do RabbitMQ

### Tarefas:
25. ✅ Criar estrutura `infra/messaging/consumers/`
26. ✅ Criar `AccountCreatedConsumer`
27. ✅ Criar `DepositConsumer` e `WithdrawConsumer`

### Exemplo:
```java
@Component
public class AccountCreatedConsumer {

    @RabbitListener(queues = RabbitMQConfig.ACCOUNT_CREATED_QUEUE)
    public void handle(AccountCreatedEvent event) {
        log.info("Processing AccountCreated: {}", event.getAccountId());

        // Enviar email de boas-vindas
        // Criar wallet inicial
        // Outras ações necessárias
    }
}
```

---

## 💰 FASE 8: Use Cases de Trading

**Objetivo**: Implementar operações principais de trading

### Tarefas:
28. ✅ Implementar `DepositUseCase`
29. ✅ Implementar `WithdrawUseCase`
30. ✅ Criar `PlaceOrderUseCase`
31. ✅ Criar `CancelOrderUseCase`
32. ✅ Criar `GetOrdersUseCase`

### Exemplo - DepositUseCase:
```java
@Service
public class DepositUseCase {

    private final AccountRepository accountRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public void execute(DepositCommand command) {
        Account account = accountRepository
            .findById(command.accountId())
            .orElseThrow(() -> new NotFoundException("Account not found"));

        account.deposit(command.assetId(), command.amount());

        accountRepository.save(account);

        account.getDomainEvents().forEach(eventPublisher::publish);
        account.clearDomainEvents();
    }
}
```

---

## 📖 FASE 9: CQRS - Query Side

**Objetivo**: Separar comandos (write) de consultas (read)

### Tarefas:
33. ✅ Criar estrutura `application/queries/`
34. ✅ Criar `AccountQueryService` (read model)
35. ✅ Criar `OrderQueryService`
36. ✅ Separar controllers em `CommandController` e `QueryController`

### Estrutura:
```
application/
├── usecases/          # Commands (write)
│   └── ...
├── queries/           # Queries (read)
│   ├── AccountQueryService.java
│   └── OrderQueryService.java
└── dtos/
```

### Benefícios:
- Otimização independente de leitura e escrita
- Escalabilidade
- Queries específicas para cada tela

---

## 🌐 FASE 10: Gateways e Integrações

**Objetivo**: Integrar com serviços externos

### Tarefas:
37. ✅ Criar estrutura `infra/gateways/`
38. ✅ Criar interface `PaymentGateway` (port)
39. ✅ Implementar `PaymentGatewayImpl` com `WebClient`
40. ✅ Criar `ExchangeGateway` para APIs externas

### Exemplo:
```java
public interface PaymentGateway {
    PaymentResult processPayment(PaymentRequest request);
}

@Service
public class StripePaymentGateway implements PaymentGateway {

    private final WebClient webClient;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        return webClient.post()
            .uri("/payments")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(PaymentResult.class)
            .block();
    }
}
```

---

## 🛡️ FASE 11: Resiliência e Retry

**Objetivo**: Adicionar tolerância a falhas

### Tarefas:
41. ✅ Adicionar Resilience4j no `pom.xml`
42. ✅ Configurar Circuit Breaker para gateways
43. ✅ Configurar Retry para chamadas externas
44. ✅ Configurar Rate Limiter

### Dependência:
```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.2.0</version>
</dependency>
```

### Configuração:
```yaml
resilience4j:
  circuitbreaker:
    instances:
      paymentGateway:
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10s

  retry:
    instances:
      exchangeGateway:
        maxAttempts: 3
        waitDuration: 1s
```

### Uso:
```java
@CircuitBreaker(name = "paymentGateway", fallbackMethod = "paymentFallback")
@Retry(name = "exchangeGateway")
public PaymentResult processPayment(PaymentRequest request) {
    // chamada externa
}
```

---

## 🚀 FASE 12: Cache com Redis

**Objetivo**: Melhorar performance com cache distribuído

### Tarefas:
45. ✅ Descomentar `spring-boot-starter-data-redis` no `pom.xml`
46. ✅ Adicionar Redis no Docker Compose
47. ✅ Criar `RedisConfig`
48. ✅ Adicionar `@Cacheable` em queries frequentes

### Docker Compose:
```yaml
redis:
  image: redis:7-alpine
  ports:
    - "6379:6379"
```

### Uso:
```java
@Cacheable(value = "accounts", key = "#accountId")
public AccountResponse getAccount(String accountId) {
    // consulta no banco
}

@CacheEvict(value = "accounts", key = "#accountId")
public void updateAccount(String accountId, UpdateRequest request) {
    // atualização
}
```

---

## 🧪 FASE 13: Testes

**Objetivo**: Garantir qualidade com cobertura de testes

### Tarefas:
49. ✅ Criar testes unitários para todos os use cases
50. ✅ Criar testes de integração para RabbitMQ (Testcontainers)
51. ✅ Criar testes para Outbox Pattern
52. ✅ Criar testes E2E para fluxo completo de trading

### Exemplo - Testcontainers:
```java
@SpringBootTest
@Testcontainers
class DepositUseCaseIntegrationTest {

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3-management");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Test
    void shouldPublishEventWhenDeposit() {
        // test
    }
}
```

---

## 📊 FASE 14: Observabilidade

**Objetivo**: Monitorar e debugar a aplicação em produção

### Tarefas:
53. ✅ Configurar métricas customizadas no Actuator
54. ✅ Adicionar logs estruturados (JSON)
55. ✅ Implementar tracing distribuído (Micrometer)
56. ✅ Criar health checks customizados

### Health Check:
```java
@Component
public class RabbitMQHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (isRabbitMQUp()) {
            return Health.up().build();
        }
        return Health.down().withDetail("reason", "RabbitMQ is down").build();
    }
}
```

---

## 📈 Progresso

Você pode acompanhar o progresso usando a todo list do Claude Code.

Use `/tasks` para ver todas as tarefas.

---

## 🎓 Referências

- [Repositório de Referência - Rodrigo Branas](https://github.com/rodrigobranas/cccat22_8)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)
- [CQRS Pattern](https://martinfowler.com/bliki/CQRS.html)
- [Domain Events](https://martinfowler.com/eaaDev/DomainEvent.html)

---

## 💡 Dicas

1. **Implemente em ordem**: As fases dependem umas das outras
2. **Teste cada fase**: Não avance sem testar
3. **Commits frequentes**: Faça commits pequenos e frequentes
4. **Documentação**: Documente decisões arquiteturais importantes
5. **Code Review**: Revise o código antes de avançar

---

## 🚨 Avisos Importantes

- ⚠️ Não pule a Fase 5 (Outbox Pattern) - é crítica para consistência
- ⚠️ Teste bem a Fase 4 (RabbitMQ) antes de seguir
- ⚠️ Use Docker Compose para desenvolvimento local
- ⚠️ Configure variáveis de ambiente para produção

---

**Bom desenvolvimento! 🚀**
