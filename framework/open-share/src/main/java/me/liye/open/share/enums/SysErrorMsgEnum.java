package me.liye.open.share.enums;

import me.liye.open.share.exception.BizExceptionMessageProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysErrorMsgEnum implements BizExceptionMessageProvider {


    // Common request and system errors
    request_params_error("400000", "Request parameter error. Error code 400"),
    system_error("500000", "System error. Error code 500"),
    sys_busy("A1000", "System busy. Error code 1000"),

    /**
     * Login/Account/Permission/Shop related
     */
    not_login("A1001", "Not logged in, please log in again"),
    account_not_exists("A1002", "Account does not exist, please register first"),
    account_profile_not_exists("A10021", "Account profile not found"),
    password_error("A1003", "Password error, please re-enter"),
    phone_duplicate("A1004", "Phone number already exists, please re-enter"),
    email_duplicate("A1005", "Email already exists, please re-enter"),
    no_permission("A1006", "No permission, please contact the administrator"),
    parameter_error("A1007", "Parameter error. Error code 1007"),
    operation_not_support("A1009", "Operation not supported. Error code 1009"),
    sms_chn_error("A1010", "SMS channel error. Error code 1010"),
    verify_code_error("A1011", "Verification code error, please re-enter"),
    captcha_error("A2011", "Captcha verification failed"),
    account_locked("A1012", "Account is locked due to too many failed attempts"),

    /**
     * Database related errors
     */
    db_error("A2001", "Database error. Error code 2001"),
    db_key_duplicate("A2002", "Database key duplicate. Error code 2002"),
    exception_overflow("A2005", "Exception overflow. Error code 2005"),
    rpc_call_error("A2006", "RPC call error. Error code 2006"),
    db_connection_error("A2007", "Database connection error. Error code 2007"),
    db_timeout("A2008", "Database operation timeout. Error code 2008"),

    /**
     * File handling errors
     */
    file_not_found("A3001", "File not found. Error code 3001"),
    file_type_not_supported("A3002", "File type not supported. Error code 3002"),
    file_size_exceeded("A3003", "File size exceeds the limit. Error code 3003"),
    file_upload_error("A3004", "File upload error. Error code 3004"),

    /**
     * Network and service related errors
     */
    service_unavailable("A4001", "Service unavailable. Error code 4001"),
    service_timeout("A4002", "Service request timeout. Error code 4002"),
    service_dependency_failure("A4003", "Service dependency failure. Error code 4003"),
    network_error("A4004", "Network error. Error code 4004"),
    dns_resolution_failed("A4005", "DNS resolution failed. Error code 4005"),

    /**
     * Authentication and authorization errors
     */
    invalid_token("A5001", "Invalid token. Error code 5001"),
    token_expired("A5002", "Token has expired. Error code 5002"),
    unauthorized_access("A5003", "Unauthorized access. Error code 5003"),
    forbidden("A5004", "Forbidden access. Error code 5004"),
    session_expired("A5005", "Session expired. Error code 5005"),
    invalid_credentials("A5006", "Invalid login credentials. Error code 5006"),

    /**
     * Payment and transaction related errors
     */
    payment_failed("A6001", "Payment failed. Error code 6001"),
    insufficient_balance("A6002", "Insufficient balance. Error code 6002"),
    transaction_declined("A6003", "Transaction declined. Error code 6003"),
    invalid_payment_method("A6004", "Invalid payment method. Error code 6004"),
    payment_timeout("A6005", "Payment timeout. Error code 6005"),

    /**
     * Shop and product related errors
     */
    shop_not_exists("B1001", "Shop does not exist. Error code B1001"),
    product_not_found("B1002", "Product not found. Error code B1002"),
    inventory_insufficient("B1003", "Insufficient inventory. Error code B1003"),
    invalid_discount("B1004", "Invalid discount or coupon. Error code B1004"),
    invalid_product_id("B1005", "Invalid product ID. Error code B1005"),

    /**
     * General validation errors
     */
    invalid_input("C1001", "Invalid input provided. Error code C1001"),
    missing_required_field("C1002", "Missing required field. Error code C1002"),
    invalid_format("C1003", "Invalid format. Error code C1003"),
    data_too_large("C1004", "Data size exceeds limit. Error code C1004"),
    unsupported_operation("C1005", "Unsupported operation. Error code C1005"),
    file_quota_exceeded("C1006", "file quota exceeds limit. Error code C1006"),

    ;
    private final String errCode;
    private final String errMsg;

    public String errorCode() {
        return errCode;
    }

    public String errorMsg() {
        return errMsg;
    }

    @Override
    public String getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errMsg;
    }
}
