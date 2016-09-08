import java.util.*;
public class device {
    public final static int DEVICE_STATE_RANDOM = 0, DEVICE_STATE_SOLVED = 1, DEVICE_STATE_TRUE_RANDOM = 2;
    public int steps;
    public final char[] colorOrder;
    public final char[] numToColor;
    public ArrayList<char[]> fh;
    public ArrayList<char[]> sh;
    public ArrayList<int[]> fhn;
    public ArrayList<int[]> shn;
    public ArrayList<char[]> solvedHalf;
    public int grav = 1;
    public int offset = 0;
    public device(){
        colorOrder = new char[] {'K','W','B','Y','G','R'};
        numToColor = new char[] {'K','W','W','B','B','B','Y','Y','Y','Y','G','G','G','G','G','R','R','R','R','R','R'};
        fh = new ArrayList<>();
        sh = new ArrayList<>();
        fhn = new ArrayList<>();
        shn = new ArrayList<>();
        for(int i = 1; i<= 6; i++){
            fh.add(new char[i]);
            sh.add(new char[i]);
            fhn.add(new int[i]);
            shn.add(new int[i]);
        }
        this.fill(device.DEVICE_STATE_SOLVED);
    }
    public device(int state){
        colorOrder = new char[] {'K','W','B','Y','G','R'};
        numToColor = new char[] {'K','W','W','B','B','B','Y','Y','Y','Y','G','G','G','G','G','R','R','R','R','R','R'};
        fh = new ArrayList<>();
        sh = new ArrayList<>();
        fhn = new ArrayList<>();
        shn = new ArrayList<>();
        solvedHalf = new ArrayList<>();
        for(int i = 1; i<= 6; i++){
            fh.add(new char[i]);
            sh.add(new char[i]);
            fhn.add(new int[i]);
            shn.add(new int[i]);
            solvedHalf.add(new char[i]);
        }
        this.fill(state);
    }
    public device(int state, int s){
        steps = s;
        colorOrder = new char[] {'K','W','B','Y','G','R'};
        numToColor = new char[] {'K','W','W','B','B','B','Y','Y','Y','Y','G','G','G','G','G','R','R','R','R','R','R'};
        fh = new ArrayList<>();
        sh = new ArrayList<>();
        fhn = new ArrayList<>();
        shn = new ArrayList<>();
        solvedHalf = new ArrayList<>();
        for(int i = 1; i<= 6; i++){
            fh.add(new char[i]);
            sh.add(new char[i]);
            fhn.add(new int[i]);
            shn.add(new int[i]);
            solvedHalf.add(new char[i]);
        }
        this.fill(state);
        this.fill(device.DEVICE_STATE_TRUE_RANDOM);
        //System.out.println("Starting to count");
        int numMoves = this.solve(-1);
        //System.out.println("Number of moves needed to solve: "+numMoves);
        //System.out.println("Solving up to: "+(numMoves-steps));
        this.solve(numMoves-steps);
    }
    private void fill(int state){
        if(state==device.DEVICE_STATE_SOLVED){
            int currBall = 0;
            for(int i = 0; i<6; i++){
                for(int j = 0; j<=i; j++){
                    sh.get(i)[j] = colorOrder[i];
                    shn.get(i)[j] = currBall;
                    currBall++;
                    solvedHalf.get(i)[j] = colorOrder[i];
                    fh.get(i)[j] = ' ';
                    fhn.get(i)[j] = -1;
                }
            }
        }else if(state==device.DEVICE_STATE_TRUE_RANDOM || state==device.DEVICE_STATE_RANDOM){
            ArrayList<Integer> numsLeft = new ArrayList<>();
            ArrayList<int[]> spotsLeft = new ArrayList<>();
            for(int i = 0; i < 21; i++){
                    numsLeft.add(i);
            }
            for(int i = 0; i <6; i++){
                for(int j = 0; j<=i; j++){
                    spotsLeft.add(new int[]{i,j,-1});
                    spotsLeft.add(new int[]{i,j,1});
                    solvedHalf.get(i)[j] = colorOrder[i];
                    sh.get(i)[j] = ' ';
                    fh.get(i)[j] = ' ';
                    fhn.get(i)[j] = -1;
                    shn.get(i)[j] = -1;
                }
            }
            for(int i = 0; i <6; i++){
                for(int j = 0; j<=i; j++){
                    int ball = (int)(Math.random()*numsLeft.size());
                    int spot = (int)(Math.random()*spotsLeft.size());
                    int[] s = spotsLeft.get(spot);
                    if(s[2] == 1){
                        fh.get(s[0])[s[1]]=numToColor[numsLeft.get(ball)];
                        fhn.get(s[0])[s[1]]=numsLeft.get(ball);
                    }else if(s[2] == -1){
                        sh.get(s[0])[s[1]]=numToColor[numsLeft.get(ball)];
                        shn.get(s[0])[s[1]]=numsLeft.get(ball);
                    }
                    spotsLeft.remove(spot);
                    numsLeft.remove(ball);
                }
            }    
        }
        offset = (int)(Math.random()*5);
        this.checkGrav();
    }
    public boolean solved(){
        for(int i = 0; i < 6; i ++){
            if(!Arrays.equals(sh.get(i),solvedHalf.get(i))){
                return false;
            }
        }
        return true;
    }
    private void checkGrav(){
        for(int i = 0; i < 6; i ++){
            char[] fwa = fh.get(i);//firsthalf working array
            int[] fwan = fhn.get(i);//firsthalf working array
            char[] swa = sh.get(nos(i));//second half working array
            int[] swan = shn.get(nos(i));//firsthalf working array
            //System.out.print(swa);
            //System.out.println("\t\tnos("+i+"): " + nos(i));
            if(grav == 1){
                for(int j = swa.length-1; j >= 0; j--){
                    if(swa[j] == ' '){
                        for(int k = j-1; k >= 0; k--){
                            if(swa[k]!=' '){
                                swa[j]=swa[k];
                                swa[k]=' ';
                                swan[j]=swan[k];
                                swan[k]=-1;
                                break;
                            }
                        }
                    }
                    if(swa[j] == ' '){
                        for(int k = 0; k < fwa.length; k++){
                            if(fwa[k]!=' '){
                                swa[j]=fwa[k];
                                fwa[k]=' ';
                                swan[j]=fwan[k];
                                fwan[k]=-1;
                                break;
                            }
                        }
                    }
                }
                for(int j = 0; j < fwa.length; j++){
                    if(fwa[j] == ' '){
                        for(int k = j; k < fwa.length; k++){
                            if(fwa[k]!=' '){
                                fwa[j]=fwa[k];
                                fwa[k]=' ';
                                fwan[j]=fwan[k];
                                fwan[k]=-1;
                                break;
                            }
                        }
                    }
                }
            }
            
            else if(grav == -1){
                for(int j = fwa.length-1; j >= 0; j--){
                    if(fwa[j] == ' '){
                        for(int k = j-1; k >= 0; k--){
                            if(fwa[k]!=' '){
                                fwa[j]=fwa[k];
                                fwa[k]=' ';
                                fwan[j]=fwan[k];
                                fwan[k]=-1;
                                break;
                            }
                        }
                    }
                    if(fwa[j] == ' '){
                        for(int k = 0; k < swa.length; k++){
                            if(swa[k]!=' '){
                                fwa[j]=swa[k];
                                swa[k]=' ';
                                fwan[j]=swan[k];
                                swan[k]=-1;
                                break;
                            }
                        }
                    }
                }
                for(int j = 0; j < swa.length; j++){
                    if(swa[j] == ' '){
                        for(int k = j; k < swa.length; k++){
                            if(swa[k]!=' '){
                                swa[j]=swa[k];
                                swa[k]=' ';
                                swan[j]=swan[k];
                                swan[k]=-1;
                                break;
                            }
                        }
                    }
                }
            }
            fh.set(i, fwa);
            sh.set(nos(i), swa);
            fhn.set(i, fwan);
            shn.set(nos(i), swan);
        }
    }
    public void flip(){
        grav *= -1;
        this.checkGrav();
    }
    public void rcw(){
        offset-=1;
        if(offset < 0){
            offset += 6;
        }
        this.checkGrav();
    }
    public void rccw(){
        offset+=1;
        if(offset > 5){
            offset -= 6;
        }
        this.checkGrav();
    }
    public int evalH(){
        int ans = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < i+1; j++){
                if(sh.get(i)[j]!=solvedHalf.get(i)[j]){
                    ans++;
                }
            }
        }
        return ans;
    }
    public void matchNumbersToColors(){
        for(int i = 0; i <6; i++){
                for(int j = 0; j<=i; j++){
                    if(fhn.get(i)[j]==-1){
                        fh.get(i)[j] = ' ';
                    }else{
                        fh.get(i)[j] = numToColor[fhn.get(i)[j]];
                    }
                    if(shn.get(i)[j]==-1){
                        sh.get(i)[j] = ' ';
                    }else{
                        sh.get(i)[j] = numToColor[shn.get(i)[j]];
                    }
                }
        }
    }
    public int[] findBall(char c){
        int row = -1;
        int col = -1;
        if(c=='R'){
            for(int i = 4; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(sh.get(i)[j]==c){
                        return new int[] {j,i};
                    }
                }
            }
        }
        if(c=='G'){
            for(int i = 3; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(sh.get(i)[j]==c){
                        return new int[] {j,i};
                    }
                }
            }
        }
        if(c=='Y'){
            for(int i = 2; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(sh.get(i)[j]==c){
                        return new int[] {j,i};
                    }
                }
            }
        }
        if(c=='B'){
            for(int i = 1; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(sh.get(i)[j]==c){
                        return new int[] {j,i};
                    }
                }
            }
        }
        if(c=='W'){
            for(int i = 0; i >= 0; i--){
                for(int j = i; j >= 0; j--){
                    if(sh.get(i)[j]==c){
                        return new int[] {j,i};
                    }
                }
            }
        }
        return new int[] {row, col};
    }
    public ArrayList<char[]> cloneHalf(ArrayList<char[]> x){
        ArrayList<char[]> ans = new ArrayList<>();
        for(int i = 1; i<= 6; i++){
            ans.add(new char[i]);
        }
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < i+1; j++){
                ans.get(i)[j] = x.get(i)[j];
            }
        }
        return ans;
    }
    public ArrayList<int[]> cloneIntHalf(ArrayList<int[]> x){
        ArrayList<int[]> ans = new ArrayList<>();
        for(int i = 1; i<= 6; i++){
            ans.add(new int[i]);
        }
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < i+1; j++){
                ans.get(i)[j] = x.get(i)[j];
            }
        }
        return ans;
    }
    public int solve(int stopAt){
        ArrayList<char[]> orig = this.cloneHalf(sh);
        if(grav == -1){
            this.flip();
        }
        for(int i = 0; i < 6; i++){
            this.rccw();
        }
        while(offset!=1){
            this.rccw();
        }
        int numMoves = 0;
        //System.out.println("Starting");
        for(int i = 5; i >= 1; i--){
            //System.out.println("working on row: " + i);
            while(!Arrays.equals(sh.get(i),solvedHalf.get(i))){
                int rowCol[] = this.findBall(colorOrder[i]);
                if(rowCol[0] == -1){//cant find ball
                    continue;
                }
                if(stopAt!= -1 && numMoves >= stopAt){
                    return 0;
                }else if(stopAt!= -1 && numMoves+rowCol[1]-rowCol[0]+1 >= stopAt){
                    this.applyManeuver(rowCol[1]+1,stopAt-numMoves);
                    return 0;
                }
                numMoves += this.applyManeuver(rowCol[1]+1,rowCol[1]-rowCol[0]+1);//apply the manuever on the col 
                while(sh.get(0)[0]==solvedHalf.get(i)[0]){
                    numMoves+=this.applyManeuver(i+1);
                    //System.out.println("Moves: "+numMoves);
                    if(stopAt!= -1 && numMoves >= stopAt){
                        return 0;
                    }
                }
            }
        }
        sh = orig;
        return numMoves;
    }
    public int applyManeuver(int col){
        int ans = 0;
        while(offset!=1){
            this.rccw();
        }
        this.flip();
        ans++;
        for(int i = 1; i < col; i++){
            this.rccw();
            ans++;
        }
        for(int i = 1; i < col; i++){
            this.rcw();
            ans++;
        }
        this.flip();
        ans++;
        for(int i = 1; i < col; i++){
            this.rccw();
            ans++;
        }
        return ans;
        /*while(offset!=1){
            this.rcw();
            ans++;
        }*/
        
    }
    public int applyManeuver(int col, int itr){
        int ans = 0;
        for(int i = 0; i < itr; i++){
            ans += this.applyManeuver(col);
        }
        return ans;
    }
    private int nos(int num){//normalize off set
        num += offset;
        if(num>5){
            num -= 6;
        }
        return num;
    }
    @Override
    public String toString(){
        String ans = "";
        ans +=       "          |"+fh.get(5)[5]+"|"+ "\n";
        ans +=       "        |"+fh.get(4)[4]+"|"+fh.get(5)[4]+"|"+ "\n";
        ans +=       "      |"+fh.get(3)[3]+"|"+fh.get(4)[3]+"|"+fh.get(5)[3]+"|"+ "\n";
        ans +=       "    |"+fh.get(2)[2]+"|"+fh.get(3)[2]+"|"+fh.get(4)[2]+"|"+fh.get(5)[2]+"|"+ "\n";
        ans +=       "  |"+fh.get(1)[1]+"|"+fh.get(2)[1]+"|"+fh.get(3)[1]+"|"+fh.get(4)[1]+"|"+fh.get(5)[1]+"|"+ "\n";
        ans +=       "|"+fh.get(0)[0]+"|"+fh.get(1)[0]+"|"+fh.get(2)[0]+"|"+fh.get(3)[0]+"|"+fh.get(4)[0]+"|"+fh.get(5)[0]+"|"+ "\n";
        ans += "-------------\n";
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                try{
                    ans+="|"+sh.get(nos(j))[i];
                    if((nos(j)== 5 && i!=0) || j==5){
                        ans+="|";
                    }
                }catch(ArrayIndexOutOfBoundsException ex){
                    if(nos(j-1)==5){
                        ans+=" ";
                    }else{
                        ans += "  ";
                    }
                }
            }
            ans += "\n";
        }
        return ans;
    }
}
