import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponManager {
    private static CouponManager instance;
    private Map<String, Coupon> coupons;
    private Map<String, User> users;

    private CouponManager() {
        this.coupons = new HashMap<>();
        this.users = new HashMap<>();
    }

    public static synchronized CouponManager getInstance() {
        if (instance == null) {
            instance = new CouponManager();
        }
        return instance;
    }

    // Coupon Management
    public synchronized void addCoupon(Coupon coupon) {
        if (coupon != null) {
            coupons.put(coupon.getCode(), coupon);
            System.out.println("✓ Coupon added: " + coupon.getCode());
        }
    }

    public synchronized Coupon getCouponByCode(String code) {
        return coupons.get(code);
    }

    public synchronized void removeCoupon(String code) {
        coupons.remove(code);
        System.out.println("✓ Coupon removed: " + code);
    }

    public synchronized void deactivateCoupon(String code) {
        Coupon coupon = coupons.get(code);
        if (coupon != null) {
            coupon.setActive(false);
            System.out.println("✓ Coupon deactivated: " + code);
        }
    }

    // User Management
    public synchronized void registerUser(User user) {
        if (user != null) {
            users.put(user.getUserId(), user);
            System.out.println("✓ User registered: " + user.getUserName());
        }
    }

    public synchronized User getUser(String userId) {
        return users.get(userId);
    }

    public synchronized void assignCouponToUser(String userId, String couponCode) {
        User user = users.get(userId);
        Coupon coupon = coupons.get(couponCode);

        if (user != null && coupon != null) {
            user.addCoupon(coupon);
            System.out.println("✓ Coupon " + couponCode + " assigned to " + user.getUserName());
        } else {
            System.out.println("✗ Unable to assign coupon: User or Coupon not found");
        }
    }

    // Apply Coupon to Order
    public synchronized boolean applyDiscount(String userId, String couponCode, Order order) {
        User user = users.get(userId);
        Coupon coupon = coupons.get(couponCode);

        if (user == null) {
            System.out.println("✗ User not found");
            return false;
        }

        if (coupon == null) {
            System.out.println("✗ Coupon not found");
            return false;
        }

        if (!user.getAvailableCoupons().contains(coupon)) {
            System.out.println("✗ Coupon is not available to this user");
            return false;
        }

        boolean success = order.applyCoupon(coupon);
        if (success) {
            user.markCouponAsUsed(coupon);
            System.out.println("✓ Coupon " + couponCode + " applied successfully");
        }
        return success;
    }

    // Display Statistics
    public synchronized void displayCouponStats() {
        System.out.println("\n=== Coupon Statistics ===");
        System.out.println("Total coupons: " + coupons.size());
        for (Coupon coupon : coupons.values()) {
            System.out.println("  - " + coupon.getCode() + ": Used " +
                             coupon.getCurrentUses() + "/" + coupon.getMaxUses() +
                             " times [" + coupon.getStatus() + "]");
        }
    }

    public synchronized void displayUserStats(String userId) {
        User user = users.get(userId);
        if (user != null) {
            System.out.println("\n=== User Statistics: " + user.getUserName() + " ===");
            System.out.println("Available Coupons: " + user.getAvailableCoupons().size());
            System.out.println("Used Coupons: " + user.getUsedCoupons().size());
        }
    }
}
