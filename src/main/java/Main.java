import java.util.Scanner;
public class Main {

    public static void playGame(){
        while(true){
            Question q = new Question("https://opentdb.com/api.php?amount=1");
            System.out.println(q);
            Scanner kb = new Scanner(System.in);
            int answer = kb.nextInt();

            if(!q.checkAnswer(answer)){
                break;
            }


        }
    }

    public static void main(String[] args) {
        //Question test = new Question("https://opentdb.com/api.php?amount=1");
        //System.out.println(test);

        playGame();

    }

}