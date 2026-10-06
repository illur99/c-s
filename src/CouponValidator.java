public class CouponValidator {

    public boolean validateCoupon(Coupon coupon, Order order) {
        if (coupon == null || order == null) {
            return false;
        }

        // Check if coupon is valid
        if (!coupon.isValid()) {
            System.out.println("Coupon is not valid (Status: " + coupon.getStatus() + ")");
            return false;
        }

        // Check minimum order value
        if (order.getTotalAmount() < coupon.getMinOrderValue()) {
            System.out.println("Order amount (₹" + order.getTotalAmount() +
                             ") is less than minimum required (₹" + coupon.getMinOrderValue() + ")");
            return false;
        }

        return true;
    }
}
