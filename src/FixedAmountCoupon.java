import java.time.LocalDate;

public class FixedAmountCoupon extends Coupon {
    private double discountAmount;
    private double maxDiscount;

    public FixedAmountCoupon(String couponId, String code, LocalDate expiryDate,
                             double minOrderValue, int maxUses, String description,
                             double discountAmount, double maxDiscount) {
        super(couponId, code, expiryDate, minOrderValue, maxUses, description);
        this.discountAmount = discountAmount;
        this.maxDiscount = maxDiscount;
    }

    @Override
    public double calculateDiscount(double orderAmount) {
        if (orderAmount >= getMinOrderValue()) {
            double discount = Math.min(discountAmount, maxDiscount);
            return Math.min(discount, orderAmount);
        }
        return 0.0;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    @Override
    public String toString() {
        return super.toString() + " [₹" + discountAmount + " OFF (Max: ₹" + maxDiscount + ")]";
    }
}
