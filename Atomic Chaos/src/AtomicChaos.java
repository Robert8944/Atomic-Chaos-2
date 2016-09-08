import java.util.*;
import javax.swing.JOptionPane;
public class AtomicChaos {
    public static void main(String[] args) {
        /*int s;
        try{
            s = Integer.parseInt(JOptionPane.showInputDialog("How Many Steps to solve?"));
            if(s<1){
                s = -1;
            }
        }catch(NumberFormatException e){
            s = -1;
        }*/
        new chaosDisp();
        /*
        device d = new device();
        int ans = 0;
        Scanner sc = new Scanner(System.in);
        while(ans != -1){
            System.out.println(d.toString());
            System.out.println("What do I do next?\n1: rotate cw\n2: rotate ccw\n3: flip\n\n-1: quit\n-2: restart with random puzzle\n-3: restart with solved puzzle");
            ans = sc.nextInt();
            switch (ans) {
                case 1:
                    d.rcw();
                    break;
                case 2:
                    d.rccw();
                    break;
                case 3:
                    d.flip();
                    break;
                case -2:
                    d = new device(device.DEVICE_STATE_TRUE_RANDOM);
                    break;
                case -3:
                    d = new device(device.DEVICE_STATE_SOLVED);
                    break;
                default:
                    break;
            }
        }
*/
    }
    
}
