package edu.sustech.cs209a.java2finalprojectdemo.helper;

public enum VirtualMachineError {
    INTERNAL_ERROR("InternalError"),
    OUT_OF_MEMORY_ERROR("OutOfMemoryError"),
    STACK_OVER_FLOW_ERROR("StackOverFlowError"),
    UNKNOWN_ERROR("UnknownError");


    private final String errorName;


    VirtualMachineError(String errorName) {
        this.errorName = errorName;
    }

    public String getName() {
        return errorName;
    }
}
