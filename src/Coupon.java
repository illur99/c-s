import java.time.LocalDate;

public abstract class Coupon {
    private String couponId;
    private String code;
    private LocalDate expiryDate;
    private double minOrderValue;
    private int maxUses;
    private int currentUses;
    private String description;
    private boolean isActive;

    public Coupon(String couponId, String code, LocalDate expiryDate,
                  double minOrderValue, int maxUses, String description) {
        this.couponId = couponId;
        this.code = code;
        this.expiryDate = expiryDate;
        this.minOrderValue = minOrderValue;
        this.maxUses = maxUses;
        this.currentUses = 0;
        this.description = description;
        this.isActive = true;
    }

    public abstract double calculateDiscount(double orderAmount);

    public boolean isValid() {
        return isActive &&
               !LocalDate.now().isAfter(expiryDate) &&
               currentUses < maxUses;
    }

    public CouponStatus getStatus() {
        if (!isActive) {
            return CouponStatus.INACTIVE;
        }
        if (LocalDate.now().isAfter(expiryDate)) {
            return CouponStatus.EXPIRED;
        }
        if (currentUses >= maxUses) {
            return CouponStatus.EXHAUSTED;
        }
        return CouponStatus.ACTIVE;
    }

    public void applyCoupon() {
        if (isValid()) {
            this.currentUses++;
        }
    }

    // Getters and Setters
    public String getCouponId() {
        return couponId;
    }

    public String getCode() {
        return code;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getCurrentUses() {
        return currentUses;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponId='" + couponId + '\'' +
                ", code='" + code + '\'' +
                ", expiryDate=" + expiryDate +
                ", status=" + getStatus() +
                ", description='" + description + '\'' +
                '}';
    }
}
