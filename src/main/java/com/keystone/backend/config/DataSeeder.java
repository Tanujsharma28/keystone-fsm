    package com.keystone.backend.config;

    import com.keystone.backend.domain.*;
    import com.keystone.backend.repository.*;
    import org.springframework.boot.ApplicationArguments;
    import org.springframework.boot.ApplicationRunner;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Component;

    @Component
    public class DataSeeder implements ApplicationRunner {

        private final AppUserRepository appUserRepository;
        private final CustomerRepository customerRepository;
        private final SiteRepository siteRepository;
        private final PartRepository partRepository;

        public DataSeeder(AppUserRepository appUserRepository,
                        CustomerRepository customerRepository,
                        SiteRepository siteRepository,
                        PartRepository partRepository) {
            this.appUserRepository = appUserRepository;
            this.customerRepository = customerRepository;
            this.siteRepository = siteRepository;
            this.partRepository = partRepository;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {

            // Already seeded check — idempotent
            if (appUserRepository.count() > 0) {
                System.out.println("=== DataSeeder: Data already exists, skipping. ===");
                return;
            }

            System.out.println("=== DataSeeder: Seeding initial data... ===");

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // --- Users ---
            AppUser admin = new AppUser();
            admin.setFullName("Admin User");
            admin.setEmail("admin@keystone.com");
            admin.setPasswordHash(encoder.encode("admin123"));
            admin.setRole(Role.MANAGER);
            admin.setActive(true);
            appUserRepository.save(admin);

            AppUser dispatcher = new AppUser();
            dispatcher.setFullName("Dispatcher One");
            dispatcher.setEmail("dispatcher@keystone.com");
            dispatcher.setPasswordHash(encoder.encode("dispatch123"));
            dispatcher.setRole(Role.DISPATCHER);
            dispatcher.setActive(true);
            appUserRepository.save(dispatcher);

            AppUser technician = new AppUser();
            technician.setFullName("Tech Raju");
            technician.setEmail("raju@keystone.com");
            technician.setPasswordHash(encoder.encode("tech123"));
            technician.setRole(Role.TECHNICIAN);
            technician.setActive(true);
            appUserRepository.save(technician);

            // --- Customer ---
            Customer customer = new Customer();
            customer.setName("Acme Corp");
            customer.setEmail("contact@acme.com");
            customer.setPhone("9876543210");
            customerRepository.save(customer);

            // --- Site ---
            Site site = new Site();
            site.setCustomer(customer);
            site.setName("Acme Delhi Office");
            site.setAddress("123 Connaught Place, New Delhi");
            siteRepository.save(site);

            // --- Parts ---
            Part p1 = new Part();
            p1.setName("HVAC Filter");
            p1.setSku("HVAC-F-001");
            p1.setStockQuantity(50);
            p1.setUnitPrice(new java.math.BigDecimal("299.00"));
            partRepository.save(p1);

            Part p2 = new Part();
            p2.setName("Circuit Breaker 20A");
            p2.setSku("CB-20A-002");
            p2.setStockQuantity(25);
            p2.setUnitPrice(new java.math.BigDecimal("850.00"));
            partRepository.save(p2);

            Part p3 = new Part();
            p3.setName("Ethernet Cable 5m");
            p3.setSku("NET-CAT6-003");
            p3.setStockQuantity(100);
            p3.setUnitPrice(new java.math.BigDecimal("149.00"));
            partRepository.save(p3);

            System.out.println("=== DataSeeder: Done. Users=3, Customer=1, Site=1, Parts=3 ===");
        }
    }