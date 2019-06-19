package cn.hikyson.methodcanary.lib;

public class MethodExitEvent extends MethodEvent {

    public MethodExitEvent(String className, int methodAccessFlag, String methodName, String methodDesc, long eventNanoTime) {
        super(className, methodAccessFlag, methodName, methodDesc, eventNanoTime);
    }

    @Override
    public String toString() {
        return "POP:et=" + eventNanoTime + ";cn=" + className + ";ma=" + methodAccessFlag + ";mn=" + methodName + ";md=" + methodDesc;
    }
}