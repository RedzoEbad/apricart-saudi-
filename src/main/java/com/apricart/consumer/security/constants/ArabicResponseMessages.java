package com.apricart.consumer.security.constants;

public class ArabicResponseMessages {

    // رسائل المصادقة
    public static final String OTP_SENT_SUCCESSFULLY_ARABIC = "تم إنشاء رمز المرور المؤقت بنجاح";
    public static final String OTP_VERIFIED_SUCCESSFULLY_ARABIC = "تم التحقق من رمز المرور المؤقت بنجاح";
    public static final String OTP_INVALID_ARABIC = "رمز المرور المؤقت غير صحيح";
    public static final String USER_INACTIVE_VERIFY_OTP_ARABIC = "المستخدم غير نشط، يرجى التحقق من رمز المرور المؤقت";
    public static final String INVALID_CREDENTIALS_ARABIC = "عذرًا، كلمة المرور أو رقم الهاتف الخاص بك غير صحيح. يرجى المحاولة مرة أخرى أو النقر فوق نسيت كلمة المرور";
    public static final String REGISTRATION_SUCCESSFUL_ARABIC = "تم التسجيل بنجاح!";
    public static final String ERROR_FAILED_ARABIC = "حدث خطأ: ";
    public static final String USER_EXISTS_ERROR_ARABIC = "المستخدم موجود بالفعل بهذا الرقم";
    public static final String INVALID_PHONE_NUMBER_ERROR_ARABIC = "رقم الهاتف غير صالح، يرجى إدخال رقم هاتف صحيح";
    public static final String USER_NOT_ACTIVE_ERROR_MESSAGE_ARABIC = "عذرًا، المستخدم غير نشط";
    public static final String INCORRECT_PASSWORD_ERROR_MESSAGE_ARABIC = "كلمة المرور الحالية غير صحيحة. يرجى إعادة إدخال كلمة المرور الصحيحة.";
    public static final String SAME_AS_OLD_PASSWORD_ERROR_MESSAGE_ARABIC = "لا يمكن أن تكون كلمة المرور الجديدة هي نفسها كلمة المرور القديمة.";
    public static final String PASSWORD_UPDATE_SUCCESS_MESSAGE_ARABIC = "لقد قمت بتعيين كلمة مرور جديدة بنجاح.";
    public static final String PROFILE_UPDATE_SUCCESS_MESSAGE_ARABIC = "لقد قمت بتحديث ملفك الشخصي بنجاح.";
    public static final String PASSWORD_UPDATE_FAILURE_MESSAGE_ARABIC = "حدث خطأ أثناء تحديث كلمة المرور. يرجى المحاولة مرة أخرى لاحقًا.";
    public static final String PROFILE_UPDATE_FAILURE_MESSAGE_ARABIC = "حدث خطأ أثناء تحديث الملف الشخصي. يرجى المحاولة مرة أخرى لاحقًا.";
    public static final String OTP_NOT_FOUND_ARABIC = "عذرًا، رمز المرور المؤقت غير موجود.";
    public static final String PASSWORD_RESET_FAILED_ARABIC = "عذرًا، فشلت إعادة تعيين كلمة المرور.";
    public static final String ACCOUNT_NOT_FOUND_ARABIC = "الحساب غير موجود.";
    public static final String PASSWORD_UPDATED_SUCCESS_ARABIC = "لقد قمت بتعيين كلمة مرور جديدة بنجاح.";
    public static final String OTP_SEND_FAILURE_ARABIC = "فشل في إرسال رمز المرور المؤقت. يرجى المحاولة مرة أخرى لاحقًا.";
    public static final String TRY_AFTER_SECONDS_ARABIC = "يرجى الانتظار لمدة %d ثانية قبل طلب رمز مرور مؤقت آخر.";
    public static final String OTP_RETRIEVED_SUCCESSFULLY_ARABIC = "تم استرجاع رمز التحقق بنجاح";

    // رسائل ذات صلة بالسلة
    public static final String PRODUCT_OUT_OF_STOCK_ARABIC = "المنتج %s غير متوفر.";
    public static final String QUANTITY_BELOW_MINIMUM_ARABIC = "الكمية المطلوبة لهذا المنتج أقل من الحد الأدنى";
    public static final String QUANTITY_EXCEEDS_MAXIMUM_ARABIC = "الكمية المضافة لهذا المنتج تتجاوز الحد الأقصى";
    public static final String QUANTITY_EXCEEDS_STOCK_ARABIC = "الكمية المطلوبة لهذا المنتج تتجاوز الحد الأقصى للمخزون";
    public static final String PRODUCT_NOT_ALLOWED_ARABIC = "غير مسموح بهذا المنتج أكثر من الحد المحدد";

    // بائع البيع
    public static final String SALE_PERSON_MARKED_AS_INACTIVE_SUCCESSFULLY_ARABIC = "تم وضع علامة على بائع البيع كغير نشط بنجاح";

    // عنوان العميل
    public static final String CUSTOMER_ADDRESS_NOT_FOUND_ARABIC = "لم يتم العثور على عنوان العميل بالمعرف (%s)";
    public static final String CUSTOMER_ADDRESS_DISABLED_SUCCESSFULLY_ARABIC = "تم تعطيل العنوان بنجاح";
    public static final String CUSTOMER_ADDRESS_ENABLED_SUCCESSFULLY_ARABIC = "تم تمكين العنوان بنجاح";
    public static final String CUSTOMER_ADDRESS_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة العنوان بنجاح";
    public static final String CUSTOMER_ADDRESS_UPDATED_SUCCESSFULLY_ARABIC = "تم تحديث العنوان بنجاح";
    public static final String CUSTOMER_ADDRESS_REFERENCE_ERROR_ARABIC = "لا يمكن إزالة العنوان لأنه مرتبط بطلب.";
    public static final String CUSTOMER_NOT_ACTIVE_ARABIC = "العميل غير نشط";
    public static final String CUSTOMER_NOT_ACTIVE_WITH_ID_ARABIC = "العميل ذو المعرف %d غير نشط";


    // العملة
    public static final String CURRENCY_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة العملة بنجاح";
    public static final String CURRENCY_STATUS_NOT_ACTIVE_ARABIC = "العملة غير نشطة.";

    // الضريبة
    public static final String TAX_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الضريبة بنجاح";
    // الفئة
    public static final String CATEGORY_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الفئة بنجاح";
    public static final String CATEGORY_NOT_FOUND_ARABIC = "لم يتم العثور على الفئة.";
    public static final String CATEGORY_STATUS_NOT_ACTIVE_ARABIC = "الفئة غير نشطة.";
    public static final String CATEGORY_NAME_EXISTS_ARABIC = "يوجد بالفعل فئة بهذا الاسم";
    public static final String CATEGORY_ARABIC_NAME_EXISTS_ARABIC = "يوجد بالفعل فئة بهذا الاسم العربي";
    // الفئة الفرعية
    public static final String SUB_CATEGORY_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الفئة الفرعية بنجاح";
    public static final String SUB_CATEGORY_NOT_FOUND_ARABIC = "لم يتم العثور على الفئة.";
    public static final String SUB_CATEGORY_NAME_EXISTS_ARABIC = "يوجد بالفعل فئة فرعية بهذا الاسم ضمن هذه الفئة";
    public static final String SUB_CATEGORY_ARABIC_NAME_EXISTS_ARABIC = "يوجد بالفعل فئة فرعية بهذا الاسم العربي ضمن هذه الفئة";

    // المستودع
    public static final String WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة المستودع بنجاح";
    public static final String WAREHOUSE_NOT_ACTIVE_ARABIC = "المستودع غير نشط";
    public static final String WAREHOUSE_NOT_FOUND_CITY_ARABIC = "تم إدخال موقع لا يتطابق مع المدينة المختارة (%s). يرجى اختيار موقع داخل الرياض أو جدة كما هو مناسب للمتابعة.";
    public static final String DELIVERY_NOT_AVAILABLE_MESSAGE_ARABIC = "نحن لا نقوم بالتوصيل هنا بعد، لكننا نتوسع بسرعة ونأمل أن نكون هنا قريبًا!";
    // قائمة الأسعار
    public static final String PRICE_LIST_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة قائمة الأسعار بنجاح";
    public static final String PRICE_LIST_STATUS_NOT_ACTIVE_ARABIC = "قائمة الأسعار غير نشطة";

    // العلامة التجارية
    public static final String BRAND_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة العلامة التجارية بنجاح";
    public static final String BRAND_NOT_FOUND_ARABIC = "لم يتم العثور على العلامة التجارية.";
    public static final String BRAND_STATUS_NOT_ACTIVE_ARABIC = "العلامة التجارية غير نشطة.";

    // المنتج
    public static final String PRODUCT_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة المنتج بنجاح";
    public static final String PRODUCT_NOT_FOUND_ARABIC = "لم يتم العثور على منتج";
    public static final String TRENDING_PRODUCT_NOT_FOUND_ARABIC = "لم يتم العثور على منتج شائع";
    public static final String DISCOUNTED_PRODUCT_NOT_FOUND_ARABIC = "لم يتم العثور على منتج مخفض السعر";
    public static final String FEATURED_PRODUCT_NOT_FOUND_ARABIC = "لم يتم العثور على منتج مميز";
    public static final String NEW_ARRIVAL_PRODUCT_NOT_FOUND_ARABIC = "لم يتم العثور على منتج واصل جديد";
    public static final String PRODUCT_NULL_ARABIC = "يجب ألا يكون المنتج فارغًا";
    public static final String PRODUCT_TITLE_EXISTS_ARABIC = "يوجد بالفعل منتج بهذا الاسم ضمن هذه الفئة الفرعية";
    public static final String PRODUCT_ARABIC_TITLE_EXISTS_ARABIC = "يوجد بالفعل منتج بهذا الاسم العربي ضمن هذه الفئة الفرعية";
    public static final String PRODUCT_SKU_EXISTS_ARABIC = "يوجد بالفعل منتج بهذا الرمز (SKU)";
    public static final String PRICE_LIST_NULL_ARABIC = "يجب ألا تكون قائمة الأسعار فارغة";
    public static final String TAX_NULL_ARABIC = "يجب ألا يكون الضريبة فارغة";
    public static final String CATEGORY_NULL_ARABIC = "يجب ألا تكون الفئة فارغة";
    public static final String SUBCATEGORY_NULL_ARABIC = "يجب ألا تكون الفئة الفرعية فارغة";
    public static final String WAREHOUSE_NULL_ARABIC = "يجب ألا يكون المستودع فارغًا";
    // مستودع المنتجات
    public static final String PRODUCTS_WAREHOUSE_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة مستودع المنتجات بنجاح";
    public static final String PRODUCT_WAREHOUSE_DUPLICATE_ARABIC = "يوجد بالفعل منتج في المستودع نفسه";
    // السلة
    public static final String CART_ITEM_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة عنصر السلة بنجاح";
    public static final String CART_CLEARED_SUCCESSFULLY_ARABIC = "تم تنظيف السلة بنجاح";
    // الخيار
    public static final String OPTION_DISABLED_SUCCESSFULLY_ARABIC = "تم تعطيل الخيار بنجاح";
    public static final String OPTION_VALUE_NOT_FOUND_ARABIC = "قيمة الخيار غير موجودة بالمفتاح: ";
    public static final String OPTION_STATUS_NOT_ACTIVE_ARABIC = "الخيار غير نشط";
    // الطلب
    public static final String ORDER_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الطلب بنجاح";
    // عنصر الطلب
    public static final String ORDER_ITEM_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة عنصر الطلب بنجاح";
    public static final String ORDER_CANCEL_TIME_EXPIRED_ARABIC = "انتهت مهلة إلغاء طلبك. يرجى الاتصال بالدعم للحصول على مزيد من المساعدة.";
    public static final String ORDER_STATUS_NULL_ARABIC = "لا يمكن أن تكون حالة الطلب فارغة.";
    //طلب معلق
    public static final String PENDING_ORDER_NOT_FOUND_ARABIC = "لم يتم العثور على طلب معلق";
    public static final String PENDING_ORDER_STATUS_NOT_ACTIVE_ARABIC = "طلب معلق غير نشطة.";



    // القسيمة
    public static final String COUPON_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة القسيمة بنجاح";
    public static final String VALID_COUPON_ARABIC = "قسيمة صالحة";
    public static final String COUPON_USAGE_LIMIT_EXCEEDS_ARABIC = "تم تجاوز حد الاستخدام لهذا الرقم الهاتفي أو معرف العميل";
    // المدينة
    public static final String CITY_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة المدينة بنجاح";
    // اللافتة
    public static final String BANNER_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة اللافتة بنجاح";
    public static final String BANNER_NOT_FOUND_ARABIC = "لم يتم العثور على اللافتة.";
    public static final String BANNER_STATUS_NOT_ACTIVE_ARABIC = "اللافتة غير نشطة.";
    //قائمة الأمنيات
    public static final String WISHLIST_NOT_FOUND_ARABIC = "لم يتم العثور على قائمة الرغبات بالمعرف (%s)";
    public static final String WISHLIST_NOT_FOUND_CUSTOMER_ARABIC = "لم يتم العثور على قائمة الرغبات بالعميل بالمعرف (%s) ومعرف المستودع (%s)";
    public static final String WISH_LIST_WAREHOUSE_NOT_FOUND_ARABIC = "قائمة الأمنيات مع هذا المستودع غير موجودة.";
    public static final String WISHLIST_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة قائمة الأمنيات بنجاح";
    public static final String WISHLIST_ITEM_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة العنصر من قائمة الأمنيات بنجاح";
    public static final String WISHLIST_CLEARED_SUCCESSFULLY_ARABIC = "تمت تصفية قائمة الأمنيات بنجاح";
    //الإعدادات الأولية
    public static final String ONBOARD_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الإعدادات الأولية بنجاح";
    public static final String ONBOARD_NOT_FOUND_ARABIC = "لم يتم العثور على البرنامج التعليمي";
    //المنتج المفقود
    public static final String MISSING_PRODUCT_SUCCESS_MESSAGE_ARABIC = "تم تقديم طلب المنتج الخاص بك بنجاح.";
    //المنزل
    public static final String HOME_DETAILS_FAILED_ARABIC = "فشل في جلب تفاصيل المنزل بواسطة معرف المستودع";
    //الإعدادات
    public static final String SETTINGS_NOT_FOUND_ARABIC = "الإعدادات غير موجودة";
    public static final String SETTINGS_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الإعداد بنجاح";
    //الأسئلة الشائعة
    public static final String FAQ_NOT_FOUND_ARABIC = "الأسئلة الشائعة غير موجودة";
    public static final String FAQ_REMOVED_SUCCESSFULLY_ARABIC = "تمت إزالة الأسئلة الشائعة بنجاح";
    //وقت التسليم
    public static final String DELIVERY_TIME_NOT_FOUND_ARABIC = "وقت التسليم غير موجود";
    public static final String DELIVERY_TIME_REMOVED_SUCCESSFULLY_ARABIC = "تم إزالة وقت التسليم بنجاح";

    //صلاحيات
    public static final String EXISTS_BY_API_URL_ARABIC = "الإذن موجود بالفعل باستخدام رابط واجهة برمجة التطبيقات الذي تم إدخاله";


    // الصورة
    public static final String IMAGE_UPLOADED_SUCCESSFULLY_ARABIC = "تم رفع الصورة بنجاح";
    public static final String ERROR_IMAGE_FAILED_ARABIC = "فشل في تحميل الصورة ";
    public static final String ERROR_INVALID_FILE_TYPE_OR_SIZE_ARABIC = ": نوع ملف غير صالح أو حجم غير صالح";

    //DataSync
    public static final String PRODUCT_NOT_FOUND_SEARCH_ARABIC = "المنتج ذو المعرف المعطى غير موجود في الفهرس.";
    public static final String PRODUCT_WAREHOUSE_NULL_SEARCH_ARABIC = "معلومات مستودع المنتج لا يمكن أن تكون فارغة.";

    //ملاحظات
    public static final String FEEDBACK_NOT_SENT_ARABIC = "فشل في إرسال بريد إلكتروني لتقديم الملاحظات إلى %s";
}
