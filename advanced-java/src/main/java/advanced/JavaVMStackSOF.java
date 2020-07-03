package advanced;

/**
 * @Author sonin
 * @Date 2020/7/3 9:56 下午
 * @Version 1.0
 **/
public class JavaVMStackSOF {
    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF javaVMStackSOF = new JavaVMStackSOF();
        try {
            javaVMStackSOF.stackLeak();
        } catch (Exception e) {
            System.out.println("stack length:" + javaVMStackSOF.stackLength);
            System.out.println(e);
        }
    }
}
