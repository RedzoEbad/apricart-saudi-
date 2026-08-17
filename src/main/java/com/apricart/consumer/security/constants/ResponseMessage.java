package com.apricart.consumer.security.constants;
public class ResponseMessage {

    // Authentication  messages
    public static final String OTP_SENT_SUCCESSFULLY = "OTP created successfully";
    public static final String OTP_VERIFIED_SUCCESSFULLY = "OTP verified successfully";
    public static final String OTP_INVALID = "OTP is incorrect";
    public static final String USER_INACTIVE_VERIFY_OTP = "User is inactive, Verify OTP";
    public static final String INVALID_CREDENTIALS="Sorry, your password or phone number is incorrect. Please try again or click forgot password";
    public static final String REGISTRATION_SUCCESSFUL = "Registration successful!";
    public static final String ERROR_FAILED = "Error occurred: ";
    public static final String USER_EXISTS_ERROR = "User exists with this phone number";
    public static final String INVALID_PHONE_NUMBER_ERROR = "Invalid phone number, Please enter correct phone number";
    public static final String USER_NOT_ACTIVE_ERROR_MESSAGE = "Sorry, User not active";
    public static final String INCORRECT_PASSWORD_ERROR_MESSAGE = "Incorrect current password. Please re-enter the correct password.";
    public static final String SAME_AS_OLD_PASSWORD_ERROR_MESSAGE = "The new password cannot be the same as the old password.";
    public static final String PASSWORD_UPDATE_SUCCESS_MESSAGE = "You’ve successfully set a new password.";
    public static final String PROFILE_UPDATE_SUCCESS_MESSAGE = "You’ve successfully updated your profile.";
    public static final String PASSWORD_UPDATE_FAILURE_MESSAGE = "An error occurred while updating the password. Please try again later.";
    public static final String PROFILE_UPDATE_FAILURE_MESSAGE = "An error occurred while updating the profile. Please try again later.";
    public static final String OTP_NOT_FOUND = "Sorry, OTP doesn't exist.";
    public static final String PASSWORD_RESET_FAILED = "Sorry, password reset failed.";
    public static final String ACCOUNT_NOT_FOUND = "Account doesn't exist.";
    public static final String ACCOUNT_DELETED_SUCCESSFULLY = "Your account has been deleted successfully.";
    public static final String ACCOUNT_DELETE_FAILED = "Failed to delete account. Please try again later.";
    public static final String PASSWORD_UPDATED_SUCCESS = "You’ve successfully set a new password.";
    public static final String OTP_RETRIEVED_SUCCESSFULLY = "OTP retrieved successfully" ;

    public static final String OTP_SEND_FAILURE = "Failed to send OTP. Please try again later.";
    public static final String TRY_AFTER_SECONDS = "Please wait for %d seconds before making another OTP request.";

    // Cart related messages
    public static final String PRODUCT_OUT_OF_STOCK = "The product %s is out of stock.";
    public static final String QUANTITY_BELOW_MINIMUM = "Your desired quantity for this product is below the minimum limit";
    public static final String QUANTITY_EXCEEDS_MAXIMUM = "Your added quantity for this product exceeds the maximum limit";
    public static final String QUANTITY_EXCEEDS_STOCK = "Your desired quantity for this product exceeds the maximum stock limit";
    public static final String PRODUCT_NOT_ALLOWED = "This product is not allowed more than the set limit";

    // Sale Person
    public static final String SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY = "Sale person marked as inactive successfully";

    // Customer Address
    public static final String CUSTOMER_ADDRESS_NOT_FOUND = "Customer Address with this id (%s) not found";
    public static final String CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY = "Address disabled successfully";
    public static final String CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY = "Address enabled successfully";
    public static final String CUSTOMER_ADDRESS_UPDATED_SUCCESSFULLY = "Address updated successfully";
    public static final String CUSTOMER_ADDRESS_REMOVED_SUCCESSFULLY = "Address removed successfully";
    public static final String CUSTOMER_ADDRESS_REFERENCE_ERROR = "Cannot remove address as it is referenced by an order";

    public static final String CUSTOMER_NOT_ACTIVE = "Customer is not active";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String CUSTOMER_NOT_ACTIVE_WITH_ID = "Customer with ID  %d is not active";

    //Currency
    public static final String CURRENCY_REMOVED_SUCCESSFULLY = "Currency removed successfully";
    public static final String CURRENCY_STATUS_NOT_ACTIVE = "Currency not active";
    //Tax
    public static final String TAX_REMOVED_SUCCESSFULLY = "Tax removed successfully";
    //Category
    public static final String CATEGORY_REMOVED_SUCCESSFULLY = "Category removed successfully";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String CATEGORY_STATUS_NOT_ACTIVE = "Category not active";
    public static final String CATEGORY_NAME_EXISTS = "A category with this name already exists";
    public static final String CATEGORY_ARABIC_NAME_EXISTS = "A category with this Arabic name already exists";
    //SubCategory
    public static final String SUB_CATEGORY_REMOVED_SUCCESSFULLY = "Sub Category removed successfully";
    public static final String SUB_CATEGORY_NOT_FOUND = "Sub Category not found";
    public static final String SUB_CATEGORY_NAME_EXISTS = "A subcategory with this name already exists in this category";
    public static final String SUB_CATEGORY_ARABIC_NAME_EXISTS = "A subcategory with this Arabic name already exists in this category";
    //Warehouse
    public static final String WAREHOUSE_REMOVED_SUCCESSFULLY = "Warehouse removed successfully";
    public static final String WAREHOUSE_NOT_ACTIVE = "Warehouse not active";
    public static final String WAREHOUSE_NOT_FOUND_CITY = "The location entered does not match your selected city (%s). Please choose a location within Riyadh or Jeddah as appropriate to continue.";
    public static final String DELIVERY_NOT_AVAILABLE_MESSAGE = "We don't deliver here yet, but we're expanding quickly and hopefully will soon!";
    //Warehouse
    public static final String PRICE_LIST_REMOVED_SUCCESSFULLY = "Price list removed successfully";
    public static final String PRICE_LIST_STATUS_NOT_ACTIVE = "Price list not active";
    //Brand
    public static final String BRAND_REMOVED_SUCCESSFULLY = "Brand removed successfully";
    public static final String BRAND_NOT_FOUND = "Brand not found";
    public static final String BRAND_STATUS_NOT_ACTIVE = "Brand not active";
    //Product
    public static final String PRODUCT_REMOVED_SUCCESSFULLY = "Product removed successfully";
    public static final String PRODUCT_NOT_FOUND = "No product found";
    public static final String TRENDING_PRODUCT_NOT_FOUND = "No trending product found";
    public static final String DISCOUNTED_PRODUCT_NOT_FOUND = "No discounted product found";
    public static final String FEATURED_PRODUCT_NOT_FOUND = "No featured product found";
    public static final String NEW_ARRIVAL_PRODUCT_NOT_FOUND = "No new arrival product found";
    public static final String PRODUCT_NULL = "Product must not be null";
    public static final String PRODUCT_TITLE_EXISTS = "A product with this name already exists in this subcategory";
    public static final String PRODUCT_ARABIC_TITLE_EXISTS = "A product with this Arabic name already exists in this subcategory";
    public static final String PRODUCT_SKU_EXISTS = "A product with this SKU already exists";
    public static final String PRICE_LIST_NULL = "PriceList must not be null";
    public static final String TAX_NULL = "Tax must not be null";
    public static final String CATEGORY_NULL = "Category must not be null";
    public static final String SUBCATEGORY_NULL = "SubCategory must not be null";
    public static final String WAREHOUSE_NULL = "Warehouse must not be null";
    //Product Warehouse
    public static final String PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY = "Products Warehouse removed successfully";
    public static final String PRODUCT_WAREHOUSE_DUPLICATE = "ProductWarehouse with the same product and warehouse already exists.";
    //Cart
    public static final String CART_ITEM_REMOVED_SUCCESSFULLY = "Cart Item removed successfully";
    public static final String CART_CLEARED_SUCCESSFULLY = "Cart cleared successfully";
    //Option
    public static final String OPTION_DISABLED_SUCCESSFULLY = "Option disabled successfully";
    public static final String OPTION_VALUE_NOT_FOUND = "Option value not found with key: ";
    public static final String OPTION_STATUS_NOT_ACTIVE = "Option not active";
    //Order
    public static final String ORDER_STATUS_NULL = "Order status can't be null";
    public static final String ORDER_REMOVED_SUCCESSFULLY = "Order removed successfully";
    public static final String ORDER_CANCEL_TIME_EXPIRED = "The time to cancel your order has expired. Please contact support for further assistance.";
    //Order Item
    public static final String ORDER_ITEM_REMOVED_SUCCESSFULLY = "Order Item removed successfully";
    //Pending Order
    public static final String PENDING_ORDER_NOT_FOUND = "No Pending Order found";
    public static final String PENDING_ORDER_STATUS_NOT_ACTIVE = "Pending Order not active";
    //Coupon
    public static final String COUPON_REMOVED_SUCCESSFULLY = "Coupon removed successfully";
    public static final String VALID_COUPON = "Valid Coupon";
    public static final String COUPON_USAGE_LIMIT_EXCEEDS = "Usage limit exceeded for this phoneNumber or customerId";
    //City
    public static final String CITY_REMOVED_SUCCESSFULLY = "City removed successfully";
    //Banner
    public static final String BANNER_REMOVED_SUCCESSFULLY = "Banner removed successfully";
    public static final String BANNER_NOT_FOUND = "Banner not found";
    public static final String BANNER_STATUS_NOT_ACTIVE = "Banner not active";
    //WishList
    public static final String WISHLIST_NOT_FOUND = "Wish List with this id (%s) not found";
    public static final String WISHLIST_NOT_FOUND_CUSTOMER = "Wish List with this customer id (%s) & warehouse id (%s) not found";
    public static final String WISH_LIST_WAREHOUSE_NOT_FOUND = "Wish List with this warehouse not found";
    public static final String WISHLIST_REMOVED_SUCCESSFULLY = "WishList removed successfully";
    public static final String WISHLIST_ITEM_REMOVED_SUCCESSFULLY = "WishList item removed successfully";
    public static final String WISHLIST_CLEARED_SUCCESSFULLY = "WishList cleared successfully";
    //OnBoard
    public static final String ONBOARD_REMOVED_SUCCESSFULLY = "OnBoard removed successfully";
    public static final String ONBOARD_NOT_FOUND = "OnBoard not found";
    //Missing Product
    public static final String MISSING_PRODUCT_SUCCESS_MESSAGE = "Your product request has been successfully submitted.";
    //Home
    public static final String HOME_DETAILS_FAILED = "Failed to fetch home details by warehouse id";
    //Settings
    public static final String SETTINGS_NOT_FOUND = "Settings not found";
    public static final String SETTINGS_REMOVED_SUCCESSFULLY = "Setting removed successfully";
    //FAQ
    public static final String FAQ_NOT_FOUND = "FAQs not found";
    public static final String FAQ_REMOVED_SUCCESSFULLY = "FAQ removed successfully";

    //DeliveryTime
    public static final String DELIVERY_TIME_NOT_FOUND = "Delivery Time not found";
    public static final String DELIVERY_TIME_REMOVED_SUCCESSFULLY = "Delivery Time removed successfully";


    public static final String EXISTS_BY_API_URL = "Permission already exists with entered API URL";

    //Image
    public static final String IMAGE_UPLOADED_SUCCESSFULLY = "Image uploaded successfully";
    public static final String ERROR_IMAGE_FAILED = "Failed to upload image ";
    public static final String ERROR_INVALID_FILE_TYPE_OR_SIZE = ": Invalid file type or size";

    //DataSync
    public static final String PRODUCT_NOT_FOUND_SEARCH = "The product with the given ID does not exist in the index.";
    public static final String PRODUCT_WAREHOUSE_NULL_SEARCH = "Product warehouse information cannot be null.";

    //Feedback
    public static final String FEEDBACK_NOT_SENT = "Failed to send feedback submission email to %s";



}

