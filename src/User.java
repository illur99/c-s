import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String userName;
    private List<Coupon> availableCoupons;
    private List<Coupon> usedCoupons;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.availableCoupons = new ArrayList<>();
        this.usedCoupons = new ArrayList<>();
    }

    public void addCoupon(Coupon coupon) {
        if (coupon != null && !availableCoupons.contains(coupon)) {
            availableCoupons.add(coupon);
        }
    }

    public void removeCoupon(Coupon coupon) {
        availableCoupons.remove(coupon);
    }

    public void markCouponAsUsed(Coupon coupon) {
        if (availableCoupons.contains(coupon)) {
            availableCoupons.remove(coupon);
            usedCoupons.add(coupon);
        }
    }

    public Coupon getCouponByCode(String code) {
        for (Coupon coupon : availableCoupons) {
            if (coupon.getCode().equals(code)) {
                return coupon;
            }
        }
        return null;
    }

    public List<Coupon> getAllValidCoupons() {
        List<Coupon> validCoupons = new ArrayList<>();
        for (Coupon coupon : availableCoupons) {
            if (coupon.isValid()) {
                validCoupons.add(coupon);
            }
        }
        return validCoupons;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<Coupon> getAvailableCoupons() {
        return new ArrayList<>(availableCoupons);
    }

    public List<Coupon> getUsedCoupons() {
        return new ArrayList<>(usedCoupons);
    }

    public void displayAvailableCoupons() {
        System.out.println("\n=== Available Coupons for " + userName + " ===");
        if (availableCoupons.isEmpty()) {
            System.out.println("No coupons available");
        } else {
            for (Coupon coupon : availableCoupons) {
                System.out.println(coupon);
            }
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", availableCoupons=" + availableCoupons.size() +
                ", usedCoupons=" + usedCoupons.size() +
                '}';
    }
}
