import java.time.LocalDate;

public class BOGOCoupon extends Coupon {
    private String applicableItem;
    private double itemDiscount;

    public BOGOCoupon(String couponId, String code, LocalDate expiryDate,
                      double minOrderValue, int maxUses, String description,
                      String applicableItem, double itemDiscount) {
        super(couponId, code, expiryDate, minOrderValue, maxUses, description);
        this.applicableItem = applicableItem;
        this.itemDiscount = itemDiscount;
    }

    @Override
    public double calculateDiscount(double orderAmount) {
        if (orderAmount >= getMinOrderValue()) {
            return itemDiscount;
        }
        return 0.0;
    }

    public String getApplicableItem() {
        return applicableItem;
    }

    public double getItemDiscount() {
        return itemDiscount;
    }

    @Override
    public String toString() {
        return super.toString() + " [BOGO on " + applicableItem + " - ₹" + itemDiscount + " OFF]";
    }
}
