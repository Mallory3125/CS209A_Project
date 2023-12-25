package edu.sustech.cs209a.java2finalprojectdemo.helper;

public enum MyVirtualMachineError {

    VIRTUAL_MACHINE_ERROR("VirtualMachineError"),
    INTERNAL_ERROR("InternalError"),
    OUT_OF_MEMORY_ERROR("OutOfMemoryError"),
    STACK_OVER_FLOW_ERROR("StackOverFlowError"),
    UNKNOWN_ERROR("UnknownError");


    private final String errorName;


    MyVirtualMachineError(String errorName) {
        this.errorName = errorName;
    }

    public String getName() {
        return errorName;
    }
}
