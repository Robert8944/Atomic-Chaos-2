import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
public class chaosDisp extends JFrame implements ActionListener, KeyListener{
    private device d;
    private JMenuBar menuBar;
    private JMenu file,edit,delaySpeed;
    private JSlider delaySlider;
    private JTextArea mainOut, hOut;
    private JPanel controlZone;
    private JButton cw, ccw, flip, solve, newPuzzle, quit, akts, amkts, solveWithAStar;
    private boolean upPressed, downPressed, leftPressed, rightPressed, pressed1, pressed2, pressed3, pressed4, pressed5, pressed6, solving, aktsPressed, amktsPressed;
    private boolean toldThem = false;
    private long lastMoveTime = 0;
    private long delay = 500;
    public chaosDisp(){
        this(-1);
    }
    public chaosDisp(int numS){
        super("Atomic Chaos");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("a.png")));
        try{UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");}catch(Exception e){}
        this.setSize(700,700);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.initilizeComponents();
        this.combineAndAddComponents();
        this.initKeys();
        this.addKeyListeners();
        this.addActionListeners();
        d = new device(device.DEVICE_STATE_TRUE_RANDOM);
        if(numS!=-1){
            d = new device(device.DEVICE_STATE_RANDOM, numS);
        }
        while(true){
            this.checkKeys();
            this.updateScreen();
            delay = delaySlider.getValue();
            if(d.solved() && !toldThem){
                JOptionPane.showMessageDialog(this, "YOU WIN!");
                toldThem = true;
            }
        }
    }
    private void initKeys(){
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        pressed1 = false;
        pressed2 = false;
        pressed3 = false;
        pressed4 = false;
        pressed5 = false;
        pressed6 = false;
        solving = false;
	aktsPressed = false;
	amktsPressed = false;
    }
    private void checkKeys(){
        if((upPressed || downPressed) && System.currentTimeMillis()-lastMoveTime >= delay){
            d.flip();
            lastMoveTime = System.currentTimeMillis();
        }else if(leftPressed && System.currentTimeMillis()-lastMoveTime >= delay){
            d.rccw();
            lastMoveTime = System.currentTimeMillis();
        }else if(rightPressed && System.currentTimeMillis()-lastMoveTime >= delay){
            d.rcw();
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed1 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(1);
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed2 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(2);
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed3 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(3);
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed4 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(4);
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed5 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(5);
            lastMoveTime = System.currentTimeMillis();
        }else if(pressed6 && System.currentTimeMillis()-lastMoveTime >= delay){
            this.doMove(6);
            lastMoveTime = System.currentTimeMillis();
        }else if(solving){
            this.solveSelf();
        }
    }
    private void combineAndAddComponents(){
        menuBar.add(file);
        menuBar.add(edit);
        file.add(delaySpeed);
        delaySpeed.add(delaySlider);
        controlZone = new JPanel();
        controlZone.setLayout(new GridLayout(3,3));
        controlZone.add(cw);
        controlZone.add(ccw);
        controlZone.add(flip);
	controlZone.add(akts);
	controlZone.add(amkts);
	controlZone.add(solveWithAStar);
        controlZone.add(solve);
        controlZone.add(newPuzzle);
        controlZone.add(quit);
        this.setJMenuBar(menuBar);
        this.add(mainOut);
        this.add(hOut,SpringLayout.NORTH);
        this.add(controlZone,SpringLayout.SOUTH);
    }
    private void initilizeComponents(){
        menuBar = new JMenuBar();
        file = new JMenu("File");
        edit = new JMenu("Edit");
        delaySpeed = new JMenu("set dealy speed");
        delaySlider = new JSlider(0,2000,150);
        mainOut = new JTextArea();
        mainOut.setEditable(false);
        mainOut.setFont(new Font("monospaced", Font.BOLD, 16));
        hOut = new JTextArea();
        hOut.setEditable(false);
        hOut.setFont(new Font("monospaced", Font.BOLD, 16));
        akts = new JButton("Anton's K till Solved");
        amkts = new JButton("anti-move K till Solved");
        solveWithAStar = new JButton("solve with A*");
        cw = new JButton("rotate clock-wise");
        ccw = new JButton("rotate counter clock-wise");
        flip = new JButton("flip the puzzle");
        solve = new JButton("go to solved state");
        newPuzzle = new JButton("get new random puzzle");
        quit = new JButton("quit");
    }
    private void addActionListeners(){
        cw.addActionListener(this);
        ccw.addActionListener(this);
        flip.addActionListener(this);
        solve.addActionListener(this);
        newPuzzle.addActionListener(this);
        quit.addActionListener(this);
    }
    private void addKeyListeners(){
        this.addKeyListener(this);
        menuBar.addKeyListener(this);
        delaySlider.addKeyListener(this);
        mainOut.addKeyListener(this);
        hOut.addKeyListener(this);
        cw.addKeyListener(this);
        ccw.addKeyListener(this);
        flip.addKeyListener(this);
        solve.addKeyListener(this);
        newPuzzle.addKeyListener(this);
        quit.addKeyListener(this);
    }
    private void updateScreen(){
        mainOut.setText(d.toString());
        hOut.setText("Heuristic Evaluation of Current State: "+d.evalH());
        this.repaint();
    }
    private void doMove(int col){
        delay = delaySlider.getValue();
        d.flip();
        updateScreen();
        try{Thread.sleep(delay);}catch(Exception ex){}
        for(int i = 1; i < col; i++){
            d.rccw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        for(int i = 1; i < col; i++){
            d.rcw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        d.flip();
        updateScreen();
        try{Thread.sleep(delay);}catch(Exception ex){}
        for(int i = 1; i < col; i++){
            d.rccw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        for(int i = 0; i < 7-col; i++){
            d.rccw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
    }
    private void doMove(int col, int itr){
        for(int i = 0; i < itr; i++){
            this.doMove(col);
        }
    }
    private void solveSelf(){
        if(d.grav == -1){
            d.flip();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        for(int i = 0; i < 6; i++){
            d.rccw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        while(d.offset!=1){
            d.rccw();
            updateScreen();
            try{Thread.sleep(delay);}catch(Exception ex){}
        }
        for(int i = 5; i >= 1; i--){
            while(!Arrays.equals(d.sh.get(i),d.solvedHalf.get(i))){
                int rowCol[] = d.findBall(d.colorOrder[i]);
                if(rowCol[0] == -1){//cant find ball
                    continue;
                }
                this.doMove(rowCol[1]+1,rowCol[1]-rowCol[0]+1);//apply the manuever on the col
                while(d.sh.get(0)[0]==d.solvedHalf.get(i)[0]){
                    this.doMove(i+1);
                }
            }
        }
        solving = false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(cw)){
            d.rcw();
        }else if(e.getSource().equals(ccw)){
            d.rccw();
        }else if(e.getSource().equals(flip)){
            d.flip();
        }else if(e.getSource().equals(solve)){
            solving = true;
        }else if(e.getSource().equals(newPuzzle)){
            d = new device(device.DEVICE_STATE_TRUE_RANDOM);
            toldThem = false;
        }else if(e.getSource().equals(quit)){
            System.exit(0);
        }else if(e.getSource().equals(akts)){
            akts = true;
        }else if(e.getSource().equals(amkts)){
            amkts = true;
        }


        updateScreen();
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_1:
                pressed1 = true;
                break;
            case KeyEvent.VK_2:
                pressed2 = true;
                break;
            case KeyEvent.VK_3:
                pressed3 = true;
                break;
            case KeyEvent.VK_4:
                pressed4 = true;
                break;
            case KeyEvent.VK_5:
                pressed5 = true;
                break;
            case KeyEvent.VK_6:
                pressed6 = true;
                break;
            case KeyEvent.VK_ENTER:
                if(e.getSource() instanceof JButton){
                    ((JButton)e.getSource()).doClick();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_1:
                pressed1 = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_2:
                pressed2 = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_3:
                pressed3 = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_4:
                pressed4 = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_5:
                pressed5 = false;
                lastMoveTime = 0;
                break;
            case KeyEvent.VK_6:
                pressed6 = false;
                lastMoveTime = 0;
                break;
            default:
                break;
        }
    }
}
