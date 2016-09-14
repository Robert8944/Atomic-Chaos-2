package atomic.chaos.pkg2;
import java.util.ArrayList;
public class ChaosPuzzle {
    public ArrayList<ArrayList<ArrayList<Integer>>> gd = new ArrayList<>();
    public ChaosPuzzle(){
        gd = newSolved();
    }
    
    public ArrayList<ArrayList<ArrayList<Integer>>> newSolved(){
        ArrayList<ArrayList<ArrayList<Integer>>> ans = new ArrayList<>();
        int ballNum = 1;
        for(int i = 0; i < 2; i++){
            ArrayList<ArrayList<Integer>> half = new ArrayList<>();
            for(int j = 0; j < 6; j++){
                ArrayList<Integer> col = new ArrayList<>();
                for(int k = 0; k < 6; k++){
                    if(i==1){
                        col.add(ballNum);
                        ballNum++;
                    }else{
                        col.add(0);
                    }
                }
                half.add(col);
            }
            ans.add(half);
        }
        return ans;
    }
    public String toString(){
        String ans = "";
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 6; j++){
                for(int k = 0; k < 6; k++){
                    ans+=""+gd.get(i).get(k).get(j);
                }
                ans+="\n";
            }
        }
        return ans;
    }
}