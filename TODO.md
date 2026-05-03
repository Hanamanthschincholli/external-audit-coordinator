# Test Fixes Progress

## Steps:
- [x] 1. Fix AuditItemRepositoryTest.shouldSoftDeleteById: Add entityManager.refresh(deleted)
- [x] 2. Fix AuditItemControllerTest: Change to @SpringBootTest @AutoConfigureMockMvc with mocks
- [ ] 3. Run `mvn clean test`
- [ ] 4. Complete
