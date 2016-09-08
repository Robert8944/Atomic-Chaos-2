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
        }else if(aktsPressed){
            this.antonsFunc(Integer.parseInt(JOptionPane.showInputDialog("How many move back do you want to be?")));
            aktsPressed = false;
        }else if(amktsPressed){
            //this.antiMoveFunc(5);
            amktsPressed = false;
        }
    }
    private boolean flat(){
        for(int i = 0; i <6; i++){
                for(int j = 0; j<=i; j++){
                    if(d.fh.get(i)[j]!=' '){
                        return false;
                    }
                }
        }
        return true;
    }
    private void antonsFunc(int wantedMoves){
        ArrayList<ArrayList<ArrayList<int[]>>> stateHistory = new ArrayList<>();
        d = new device(device.DEVICE_STATE_TRUE_RANDOM);
        boolean lastMoveWasFlip = true;
        while(!flat()){
            ArrayList<ArrayList<int[]>> temp = new ArrayList<>();
            temp.add(d.cloneIntHalf(d.fhn));
            temp.add(d.cloneIntHalf(d.shn));
            stateHistory.add(temp);
            if(!lastMoveWasFlip && Math.random()>.66667){
                d.flip();
            }else{
                lastMoveWasFlip = false;
                if(Math.random()>.5){
                    d.rcw();
                }else{
                    d.rccw();
                }
            }
        }
        int spotInArray = stateHistory.size() - wantedMoves;
        //begin restickering
        ArrayList<ArrayList<int[]>> spot = stateHistory.get(spotInArray);
        ArrayList<int[]> spotsh = spot.get(1);
        ArrayList<int[]> spotfh = spot.get(0);
        int[] changeArray = new int[21];
        int pos = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < i+1; j++){
                changeArray[d.shn.get(i)[j]] = pos;
                pos++;
            }
        }
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < i+1; j++){
                if(spotsh.get(i)[j]!=-1){
                    spotsh.get(i)[j] = changeArray[spotsh.get(i)[j]];
                }
                if(spotfh.get(i)[j]!=-1){
                    spotfh.get(i)[j] = changeArray[spotfh.get(i)[j]];
                }
            }
        }
        d.fhn = spotfh;
        d.shn = spotsh;
        d.matchNumbersToColors();
    }
    private int numBalls(char[] a){
        int ans = 0;
        for(int i = 0; i < a.length; i++){
            if(a[i]!=' '){
                ans++;
            }
        }
        return ans;
    }
    private void antiMoveFunc(int x){
        /*boolean lastWasFlip = true;
        for(int i = 0;i < x; i ++){
            if(!lastWasFlip && Math.random()>.6){
                //doFlip
            }else{
                int numCols = (int)(Math.random()*2)+1;
                int[] colsToBeMoved = new int[numCols];
                for(int j = 0; j < numCols; j++){
                    colsToBeMoved[i]=(int)(Math.random()*6);
                }
                for(int j = 0; j < numCols; j++){
                    int colToBeMoved = colsToBeMoved[i];
                    if(d.grav == 1){
                        if((numBalls(d.sh.get(colToBeMoved-1)) == colToBeMoved-1 || numBalls(d.sh.get(colToBeMoved+1)) == colToBeMoved+1) && d.sh.get(colToBeMoved)[colToBeMoved]!= ' '){
                            int NumBallsToMove = (int)(Math.random()*numBalls(d.sh.get(colToBeMoved)));
                            if( numBalls(d.sh.get(colToBeMoved-1)) == colToBeMoved-1 ){
                                
                            }
                        }
                    }else if(d.grav == -1){
                        if((numBalls(d.fh.get(colToBeMoved-1)) == colToBeMoved-1 || numBalls(d.fh.get(colToBeMoved+1)) == colToBeMoved+1) && d.fh.get(colToBeMoved)[colToBeMoved]!= ' '){
                            int NumBallsToMove = (int)(Math.random()*numBalls(d.fh.get(colToBeMoved)));
                        }
                    }
                }
            }
        }*/
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
        akts.addActionListener(this);
        amkts.addActionListener(this);
        solveWithAStar.addActionListener(this);
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
            aktsPressed = true;
        }else if(e.getSource().equals(amkts)){
            amktsPressed = true;
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
