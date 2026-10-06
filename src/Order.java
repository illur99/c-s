public class Order {
    private String orderId;
    private double totalAmount;
    private Coupon appliedCoupon;
    private double discountAmount;
    private double finalAmount;

    public Order(String orderId, double totalAmount) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.appliedCoupon = null;
        this.discountAmount = 0.0;
        this.finalAmount = totalAmount;
    }

    public boolean applyCoupon(Coupon coupon) {
        if (coupon == null || !coupon.isValid()) {
            return false;
        }

        CouponValidator validator = new CouponValidator();
        if (!validator.validateCoupon(coupon, this)) {
            return false;
        }

        this.appliedCoupon = coupon;
        this.discountAmount = coupon.calculateDiscount(totalAmount);
        this.finalAmount = totalAmount - discountAmount;
        coupon.applyCoupon();

        return true;
    }

    public boolean removeCoupon() {
        if (appliedCoupon != null) {
            this.appliedCoupon = null;
            this.discountAmount = 0.0;
            this.finalAmount = totalAmount;
            return true;
        }
        return false;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Coupon getAppliedCoupon() {
        return appliedCoupon;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", totalAmount=₹" + totalAmount +
                ", discount=₹" + discountAmount +
                ", finalAmount=₹" + finalAmount +
                ", coupon=" + (appliedCoupon != null ? appliedCoupon.getCode() : "None") +
                '}';
    }
}
