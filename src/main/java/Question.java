
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


import static org.apache.commons.text.StringEscapeUtils.unescapeHtml4;

public class Question {


//    {
//        "response_code":0,
//        "results":[{
//            "category":"Entertainment: Japanese Anime & Manga",
//            "type":"multiple",
//            "difficulty":"medium",
//            "question":"In Dragon Ball Z, who was the first character to go Super Saiyan 2?",
//            "correct_answer":"Gohan",
//            "incorrect_answers":["Goku", "Vegeta", "Trunks"]
//            }
//        ]
//    }


//        "category":"History",
//        "type":"multiple",
//        "difficulty":"medium",
//        "question":"Who was the leader of the Communist Party of Yugoslavia ?",
//        "correct_answer":"Josip Broz Tito",
//        "incorrect_answers":["Karadjordje Petrovic", "Milos Obilic", "Aleskandar Petrovic"]

    private String category;
    private String type;

    private String difficulty;
    private String question;

    private String correctAnswer;
    private JSONArray incorrectAnswers;

    private int correctAnswerIndex;

    private String[] answerOptions;

    private ArrayList<String> listIncorrectAnswers;

    public static String getToken() {
        return token;
    }

    private static String token;

    public static void setToken() {
        String t = "";

        try {
            URL url = new URL("https://opentdb.com/api_token.php?command=request");


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();


                JSONParser parse = new JSONParser();
                JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(informationString));


                t = (String) dataObject.get("token");


            }
        } catch (Exception e) {
            System.err.println("There was an error with getting the token.");
        }
        token = t;
        System.out.println(token);
    }

    static {
        setToken();
    }

    public Question(String apiUrl) {


        //https://gist.github.com/Da9el00/e8b1c2e5185e51413d9acea81056c2f9
        //Modified the code the above on how to manipulate JSON from APIs.
        try {
//            new Session();
//            String token = Session.getToken();
            // Question.setToken();
            System.out.println(token);
            URL url = new URL((apiUrl + "&token=" + token));


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();

            // 200 OK
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                //Close the scanner
                scanner.close();


                String info = "";
                info = informationString.substring(informationString.indexOf(",") + 1, informationString.length() - 1);
                info = info.substring(info.indexOf(":") + 1);


                //JSON simple library Setup with Maven is used to convert strings to JSON
                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(info));

                //Get the first JSON object in the JSON array
                System.out.println(dataObject.get(0));

                JSONObject q = (JSONObject) dataObject.get(0);


                category = unescapeHtml4((String) q.get("category"));
                type = unescapeHtml4((String) q.get("type"));
                difficulty = unescapeHtml4((String) q.get("difficulty"));
                question = unescapeHtml4((String) q.get("question"));
                correctAnswer = unescapeHtml4((String) q.get("correct_answer"));
                System.out.println(q.get("incorrect_answers"));

                incorrectAnswers = (JSONArray) q.get("incorrect_answers");

                listIncorrectAnswers = new ArrayList<>();

                for (int i = 0; i < incorrectAnswers.size(); i++) {
                    listIncorrectAnswers.add(unescapeHtml4((String) incorrectAnswers.get(i)));
                }

                Random random = new Random();
                correctAnswerIndex = random.nextInt(0, incorrectAnswers.size() - 1);

                answerOptions = new String[1 + incorrectAnswers.size()];

                answerOptions[correctAnswerIndex] = correctAnswer + " correct";

                int j = 0;
                for (int i = 0; i < answerOptions.length; i++) {
                    if (i != correctAnswerIndex) {
                        answerOptions[i] = (String) incorrectAnswers.get(j);
                        j++;
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("There was an error with getting the question.");
        }


    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder();

        /*toReturn.append(category);
        toReturn.append("\n");
        toReturn.append(type);
        toReturn.append("\n");
        toReturn.append(difficulty);
        toReturn.append("\n");
        toReturn.append(question);
        toReturn.append("\n");
        toReturn.append(correctAnswer);
        toReturn.append("\n");*/

        toReturn.append("Category: " + category + "\n");
        toReturn.append("Difficulty: " + difficulty + "\n");
        toReturn.append("Question: " + question + "\n");

        for (int i = 0; i < answerOptions.length; i++) {
            toReturn.append((i + 1) + ". " + answerOptions[i]);

            if (i == answerOptions.length - 1) {
                break;
            }

            toReturn.append("\n");
        }


//        for (int i = 0; i < listIncorrectAnswers.size(); i++) {
//            toReturn.append(listIncorrectAnswers.get(i));
//            if (i == listIncorrectAnswers.size() - 1) {
//                break;
//            }
//            toReturn.append("\n");
//        }

        return toReturn.toString();
    }

}


