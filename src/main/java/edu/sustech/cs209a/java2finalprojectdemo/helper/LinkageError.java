package edu.sustech.cs209a.java2finalprojectdemo.helper;

public enum LinkageError {
    BOOTSTRAP_METHOD_ERROR("BootstrapMethodError"),
    CLASS_CIRCULARITY_ERROR("ClassCircularityError"),
    CLASS_FORMAT_ERROR("ClassFormatError"),
    UNSUPPORTED_CLASS_VERSION_ERROR("UnsupportedClassVersionError"),
    EXCEPTION_IN_INITIALIZER_ERROR("ExceptionInInitializerError"),
    INCOMPATIBLE_CLASS_CHANGE_ERROR("IncompatibleClassChangeError"),
    ABSTRACT_METHOD_ERROR("AbstractMethodError"),
    ILLEGAL_ACCESS_ERROR("IllegalAccessError"),
    INSTANTIATION_ERROR("InstantiationError"),
    NO_SUCH_FIELD_ERROR("NoSuchFieldError"),
    NO_SUCH_METHOD_ERROR("NoSuchMethodError"),
    NO_CLASS_DEF_FOUND_ERROR("NoClassDefFoundError"),
    UNSATISFIED_LINK_ERROR("UnsatisfiedLinkError"),
    VERIFY_ERROR("VerifyError");

    private final String errorName;


    LinkageError(String errorName) {
        this.errorName = errorName;
    }

    public String getName() {
        return errorName;
    }
}
