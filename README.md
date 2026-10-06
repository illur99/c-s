# Designing a Coupon System for Swiggy

## Overview
The Swiggy Coupon System is a low-level design (LLD) implementation that demonstrates how to build a robust coupon management system. It handles various coupon types, user assignments, order processing, and discount calculations with thread-safe operations.

## Requirements

### Functional Requirements

1. **Coupon Management**: Create, update, deactivate, delete, and retrieve coupons
2. **Multiple Coupon Types**: Percentage-based, fixed-amount, and BOGO offers
3. **Coupon Validity**: Expiry date, minimum order value, max usage limits
4. **Order Processing**: Apply/remove coupons, calculate discounts, track final amount
5. **Coupon Usage Tracking**: Monitor usage vs. max, track status (Active, Expired, Exhausted, Inactive)
6. **User-Coupon Mapping**: Assign coupons to users, maintain available/used coupons
7. **Validation**: Validate coupon status, minimum amount, usage limits, user access
8. **Statistics**: Display coupon usage, user statistics, discount metrics

### Non-Functional Requirements

1. **Performance**: < 100ms coupon lookup, < 1ms discount calculation, < 500ms order processing
2. **Scalability**: Support millions of coupons, unlimited users, 1000+ concurrent applications
3. **Thread Safety**: Synchronized operations, prevent race conditions, atomic coupon applications
4. **Reliability**: Data consistency, prevent double-usage, graceful error recovery
5. **Security**: Input validation, prevent unauthorized access and coupon spoofing
6. **Maintainability**: Clean code, separation of concerns, design patterns, proper documentation
7. **Extensibility**: Support new coupon types without code changes, pluggable validation rules
8. **Usability**: Clear APIs, meaningful error messages, intuitive coupon display
9. **Availability**: 24/7 uptime, graceful failure handling, minimal maintenance downtime
10. **Code Quality**: High reusability, low coupling, SOLID principles, type-safe operations

## Architecture

### UML Class Diagram

```
┌─────────────────────────┐
│    CouponManager        │ (Singleton)
│  ─────────────────────  │
│  - coupons: Map         │
│  - users: Map           │
│  ─────────────────────  │
│  + getInstance()        │
│  + addCoupon()          │
│  + getCouponByCode()    │
│  + registerUser()       │
│  + applyDiscount()      │
│  + displayStats()       │
└─────────────────────────┘
         │
         ├─────────────────────────────────────┐
         │                                     │
    ┌────▼────────────────────┐        ┌──────▼─────────┐
    │      Coupon             │        │      User       │
    │   (Abstract)            │        │  ─────────────  │
    │ ─────────────────────   │        │ - userId        │
    │ - couponId              │        │ - userName      │
    │ - code                  │        │ - available[]   │
    │ - expiryDate            │        │ - used[]        │
    │ - minOrderValue         │        │ ─────────────   │
    │ - maxUses               │        │ + addCoupon()   │
    │ - currentUses           │        │ + getCoupon()   │
    │ ─────────────────────   │        │ + getCoupon()   │
    │ + calculateDiscount()   │        │ + getValid()    │
    │ + isValid()             │        │ + markAsUsed()  │
    │ + getStatus()           │        └─────────────────┘
    │ + applyCoupon()         │
    └────┬────────────────────┘
         │
    ┌────┴────────────────┬──────────────────┬─────────────┐
    │                     │                  │             │
 ┌──▼─────────────┐ ┌────▼──────────┐ ┌────▼───────────┐
 │ Percentage     │ │ FixedAmount    │ │   BOGO         │
 │   Coupon       │ │   Coupon       │ │   Coupon       │
 │ ─────────────  │ │ ─────────────  │ │ ─────────────  │
 │ - discount%    │ │ - amount       │ │ - itemName     │
 │                │ │ - maxDiscount  │ │ - discount     │
 │ + calculate()  │ │ + calculate()  │ │ + calculate()  │
 └────────────────┘ │                │ │                │
                    │ + calculate()  │ │ + calculate()  │
                    └────────────────┘ └────────────────┘

         ┌─────────────────────┐
         │      Order          │
         │  ─────────────────  │
         │ - orderId           │
         │ - totalAmount       │
         │ - appliedCoupon     │
         │ - discountAmount    │
         │ - finalAmount       │
         │  ─────────────────  │
         │ + applyCoupon()     │
         │ + removeCoupon()    │
         │ + getDiscount()     │
         └─────────────────────┘

         ┌───────────────────────┐
         │  CouponValidator      │
         │  ───────────────────  │
         │ + validateCoupon()    │
         └───────────────────────┘

         ┌───────────────────────┐
         │   CouponStatus        │
         │  (ENUM)               │
         │  ───────────────────  │
         │ - ACTIVE              │
         │ - EXPIRED             │
         │ - EXHAUSTED           │
         │ - INACTIVE            │
         └───────────────────────┘
```

## Class Implementations

### 1. **CouponStatus** (Enum)
Defines the different states a coupon can be in:
- `ACTIVE`: Coupon is valid and can be used
- `EXPIRED`: Coupon has passed its expiry date
- `EXHAUSTED`: Maximum usage limit reached
- `INACTIVE`: Coupon has been deactivated

### 2. **Coupon** (Abstract Base Class)
Represents a generic coupon with common properties:
- `couponId`: Unique identifier
- `code`: Coupon code (e.g., "WELCOME20")
- `expiryDate`: Expiration date
- `minOrderValue`: Minimum order amount to apply
- `maxUses`: Maximum times this coupon can be used
- `currentUses`: Current usage count

**Key Methods**:
- `calculateDiscount(orderAmount)`: Abstract method for discount calculation
- `isValid()`: Checks if coupon can be applied
- `getStatus()`: Returns current coupon status
- `applyCoupon()`: Increments usage counter

### 3. **PercentageCoupon** (Concrete Implementation)
Discount based on percentage of order amount:
- Stores `discountPercent` (e.g., 20% for "20% OFF")
- Calculates discount as `(orderAmount * discountPercent) / 100`

Example: 20% off on ₹500 order = ₹100 discount

### 4. **FixedAmountCoupon** (Concrete Implementation)
Flat discount amount regardless of order size:
- Stores `discountAmount` and `maxDiscount`
- Applies the lesser of discount amount or max discount

Example: ₹100 off (max ₹150) on ₹500 order = ₹100 discount

### 5. **BOGOCoupon** (Concrete Implementation)
Buy-One-Get-One offers on specific items:
- Stores `applicableItem` and `itemDiscount`
- Provides fixed discount on specified items

Example: ₹50 off on Biryani orders

### 6. **Order** (Data Model)
Represents a customer order:
- Tracks original amount, applied coupon, and final amount
- Supports coupon application and removal
- Automatically calculates discounts

### 7. **User** (Entity)
Represents a customer with coupon inventory:
- Maintains list of available coupons
- Tracks used coupons
- Can query coupons by code
- Supports coupon assignment and usage tracking

### 8. **CouponValidator** (Validator)
Validates coupon applicability:
- Checks coupon validity status
- Verifies minimum order value requirement
- Ensures other business rules are met

### 9. **CouponManager** (Singleton)
Central management system for entire coupon ecosystem:
- **Thread-Safe**: Uses `synchronized` methods
- **Coupon Management**: Add, remove, deactivate coupons
- **User Management**: Register users, assign coupons
- **Discount Application**: Apply coupons to orders
- **Statistics**: Track coupon and user statistics

## Design Patterns Used

### 1. **Singleton Pattern**
`CouponManager` uses Singleton to ensure only one instance manages all coupons:
```java
public static synchronized CouponManager getInstance() {
    if (instance == null) {
        instance = new CouponManager();
    }
    return instance;
}
```

### 2. **Strategy Pattern**
Different coupon types implement different discount calculation strategies:
- `PercentageCoupon`: Calculates percentage-based discount
- `FixedAmountCoupon`: Applies fixed discount
- `BOGOCoupon`: Implements BOGO logic

Each has its own `calculateDiscount()` implementation.

### 3. **Abstract Factory Pattern** (Optional Extension)
Could create a `CouponFactory` to instantiate coupon types:
```java
CouponFactory factory = new CouponFactory();
Coupon coupon = factory.createCoupon(CouponType.PERCENTAGE, ...);
```

### 4. **Observer Pattern** (Optional Extension)
Could notify users when:
- Coupons are about to expire
- New coupons become available
- Coupon status changes

### 5. **Template Method Pattern**
Abstract `Coupon` class defines the template for validation and discount calculation, with subclasses providing specific implementations.

## Key Features

### Thread Safety
- `CouponManager` uses `synchronized` keyword on all public methods
- Ensures thread-safe operations during concurrent coupon applications
- Prevents race conditions during coupon usage updates

### Coupon Validation
- Checks expiry date
- Verifies minimum order value
- Ensures usage limit not exceeded
- Validates coupon active status

### Order Processing
- Apply coupon to order
- Calculate discount automatically
- Track final amount after discount
- Support coupon removal and reapplication

### User Coupon Management
- Assign coupons to users
- Track available and used coupons
- Query user-specific coupons
- Mark coupons as used

## Usage Example

```java
// Get singleton instance
CouponManager manager = CouponManager.getInstance();

// Create and add a coupon
Coupon coupon = new PercentageCoupon(
    "COUP001", "WELCOME20", LocalDate.of(2026, 12, 31),
    300.0, 50, "Welcome offer - 20% off", 20.0
);
manager.addCoupon(coupon);

// Register a user
User user = new User("USER001", "John Doe");
manager.registerUser(user);

// Assign coupon to user
manager.assignCouponToUser("USER001", "WELCOME20");

// Create order and apply coupon
Order order = new Order("ORD001", 500.0);
manager.applyDiscount("USER001", "WELCOME20", order);

// Output: Order amount reduced from ₹500 to ₹400
```

## Time Complexity

| Operation | Complexity |
|-----------|-----------|
| Add Coupon | O(1) |
| Get Coupon | O(1) |
| Assign Coupon | O(1) |
| Apply Discount | O(1) |
| Display Statistics | O(n) where n = total coupons |
| Validate Coupon | O(1) |

## Space Complexity
- O(m + n) where m = number of coupons and n = number of users

## Running the Demo

The `Main.java` file contains a comprehensive demonstration:

1. Creates 3 different types of coupons
2. Registers 3 users
3. Assigns coupons to users
4. Creates multiple orders and applies coupons
5. Tests edge cases (invalid amounts, deactivated coupons)
6. Displays statistics

**Output includes**:
- Coupon creation and assignment
- Order processing with discounts
- Coupon status tracking
- Usage statistics
- Edge case handling

## Possible Extensions

1. **Category-Based Coupons**: Restrict coupons to specific restaurant categories
2. **Time-Window Coupons**: Coupons valid only during specific hours
3. **Loyalty Points Integration**: Combine coupons with loyalty program
4. **Referral Coupons**: Generate unique coupons for user referrals
5. **Coupon Stacking**: Allow multiple coupons on single order (with rules)
6. **Analytics Dashboard**: Track coupon performance and user preferences
7. **Payment Method Restrictions**: Coupons valid only for specific payment methods
8. **Geo-Based Coupons**: Location-specific coupon availability

## Testing Strategy

The system includes test cases for:
- ✓ Percentage-based discount calculation
- ✓ Fixed-amount discount calculation
- ✓ BOGO offer application
- ✓ Minimum order value validation
- ✓ Coupon expiry validation
- ✓ Maximum usage limit enforcement
- ✓ Coupon deactivation handling
- ✓ User-coupon assignment
- ✓ Concurrent coupon applications
- ✓ Order discount calculation

## Conclusion

The Swiggy Coupon System demonstrates:
- Clean separation of concerns
- Extensible design using inheritance and polymorphism
- Thread-safe concurrent operations
- Proper validation and error handling
- Singleton pattern for centralized management
- Strategy pattern for different discount types

This LLD can be extended to include database persistence, REST APIs, and advanced features as needed.
