import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              🍔 SWIGGY COUPON SYSTEM - DEMO 🍔                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");

        CouponManager manager = CouponManager.getInstance();

        // ===== STEP 1: Create and Add Coupons =====
        System.out.println("\n📋 STEP 1: Creating Coupons...\n");

        Coupon percentageCoupon = new PercentageCoupon(
                "COUP001",
                "WELCOME20",
                LocalDate.of(2026, 12, 31),
                300.0,
                50,
                "Welcome offer - 20% off",
                20.0
        );

        Coupon fixedAmountCoupon = new FixedAmountCoupon(
                "COUP002",
                "FLAT100",
                LocalDate.of(2026, 12, 31),
                500.0,
                30,
                "Flat ₹100 off",
                100.0,
                150.0
        );

        Coupon bogoCoupon = new BOGOCoupon(
                "COUP003",
                "BOGO50",
                LocalDate.of(2026, 12, 31),
                400.0,
                20,
                "Buy one get one on selected items",
                "Biryanis",
                50.0
        );

        manager.addCoupon(percentageCoupon);
        manager.addCoupon(fixedAmountCoupon);
        manager.addCoupon(bogoCoupon);

        // ===== STEP 2: Register Users =====
        System.out.println("\n👥 STEP 2: Registering Users...\n");

        User user1 = new User("USER001", "Raj Kumar");
        User user2 = new User("USER002", "Priya Singh");
        User user3 = new User("USER003", "Amit Patel");

        manager.registerUser(user1);
        manager.registerUser(user2);
        manager.registerUser(user3);

        // ===== STEP 3: Assign Coupons to Users =====
        System.out.println("\n🎁 STEP 3: Assigning Coupons to Users...\n");

        manager.assignCouponToUser("USER001", "WELCOME20");
        manager.assignCouponToUser("USER001", "FLAT100");
        manager.assignCouponToUser("USER002", "WELCOME20");
        manager.assignCouponToUser("USER002", "BOGO50");
        manager.assignCouponToUser("USER003", "FLAT100");

        // Display available coupons
        user1.displayAvailableCoupons();
        user2.displayAvailableCoupons();
        user3.displayAvailableCoupons();

        // ===== STEP 4: Create Orders and Apply Coupons =====
        System.out.println("\n🛒 STEP 4: Creating Orders and Applying Coupons...\n");

        // Test Case 1: User1 with WELCOME20 coupon
        System.out.println("\n--- Test Case 1: Raj Kumar applies WELCOME20 on ₹500 order ---");
        Order order1 = new Order("ORD001", 500.0);
        System.out.println("Order before coupon: " + order1);
        manager.applyDiscount("USER001", "WELCOME20", order1);
        System.out.println("Order after coupon:  " + order1);

        // Test Case 2: User2 with FLAT100 coupon
        System.out.println("\n--- Test Case 2: Priya Singh applies FLAT100 on ₹600 order ---");
        Order order2 = new Order("ORD002", 600.0);
        System.out.println("Order before coupon: " + order2);
        manager.applyDiscount("USER002", "BOGO50", order2);
        System.out.println("Order after coupon:  " + order2);

        // Test Case 3: User3 with FLAT100 coupon
        System.out.println("\n--- Test Case 3: Amit Patel applies FLAT100 on ₹800 order ---");
        Order order3 = new Order("ORD003", 800.0);
        System.out.println("Order before coupon: " + order3);
        manager.applyDiscount("USER003", "FLAT100", order3);
        System.out.println("Order after coupon:  " + order3);

        // Test Case 4: Try to apply expired coupon (edge case)
        System.out.println("\n--- Test Case 4: Trying to apply coupon on order below minimum amount ---");
        Order order4 = new Order("ORD004", 200.0);
        System.out.println("Order before coupon: " + order4);
        manager.applyDiscount("USER001", "FLAT100", order4);
        System.out.println("Order after coupon:  " + order4);

        // Test Case 5: Remove coupon and reapply
        System.out.println("\n--- Test Case 5: Remove and reapply coupon ---");
        Order order5 = new Order("ORD005", 750.0);
        System.out.println("Original order: " + order5);
        manager.applyDiscount("USER001", "WELCOME20", order5);
        System.out.println("After coupon 1: " + order5);
        order5.removeCoupon();
        System.out.println("After removing:  " + order5);

        // ===== STEP 5: Display Statistics =====
        System.out.println("\n📊 STEP 5: Displaying Statistics...");
        manager.displayCouponStats();
        manager.displayUserStats("USER001");
        manager.displayUserStats("USER002");
        manager.displayUserStats("USER003");

        // ===== STEP 6: Deactivate a Coupon =====
        System.out.println("\n🔒 STEP 6: Deactivating Coupon...\n");
        manager.deactivateCoupon("BOGO50");

        // Try applying deactivated coupon
        System.out.println("\n--- Test Case 6: Try applying deactivated coupon ---");
        Order order6 = new Order("ORD006", 500.0);
        System.out.println("Order before coupon: " + order6);
        manager.applyDiscount("USER002", "BOGO50", order6);
        System.out.println("Order after coupon:  " + order6);

        // ===== Final Statistics =====
        System.out.println("\n📊 Final Statistics:");
        manager.displayCouponStats();

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       DEMO COMPLETED ✓                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝\n");
    }
}
