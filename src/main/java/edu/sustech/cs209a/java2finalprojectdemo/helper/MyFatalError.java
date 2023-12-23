package edu.sustech.cs209a.java2finalprojectdemo.helper;

public enum MyFatalError {
    EXCEPTION_IN_INITIALIZER("ExceptionInInitializerError"),
    INTERNAL_ERROR("InternalError"),
    NO_CLASS_DEF_FOUND_ERROR("NoClassDefFoundError"),
    NO_SUCH_METHOD_ERROR("NoSuchMethodError"),
    OUT_OF_MEMORY_ERROR("OutOfMemoryError"),
    STACK_OVERFLOW_ERROR("StackOverflowError"),
    UNSATISFIED_LINK_ERROR("UnsatisfiedLinkError"),
    UNSUPPORTED_CLASS_VERSION_ERROR("UnsupportedClassVersionError");


    private final String errorName;

    MyFatalError(String errorName) {
        this.errorName = errorName;
    }

    public String getName() {
        return errorName;
    }
}