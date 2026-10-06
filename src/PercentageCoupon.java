import java.time.LocalDate;

public class PercentageCoupon extends Coupon {
    private double discountPercent;

    public PercentageCoupon(String couponId, String code, LocalDate expiryDate,
                            double minOrderValue, int maxUses, String description,
                            double discountPercent) {
        super(couponId, code, expiryDate, minOrderValue, maxUses, description);
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculateDiscount(double orderAmount) {
        if (orderAmount >= getMinOrderValue()) {
            return (orderAmount * discountPercent) / 100.0;
        }
        return 0.0;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + discountPercent + "% OFF]";
    }
}
