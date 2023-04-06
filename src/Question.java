import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Question {
    private float mark;
    private String questionTitle;
    private String[] answers;
    private int correctAnswer;

    private Exam exam;

    private int databaseID;

    //Construct the question
    public Question(float mark, String questionTitle, String[] answers, int correctAnswer, Exam exam) {
        this.mark = mark;
        this.questionTitle = questionTitle;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.exam = exam;
    }

    //Construct when getting from database
    public Question(int databaseID, float mark, String questionTitle, String[] answers, int correctAnswer) {
        this.databaseID = databaseID;
        this.mark = mark;
        this.questionTitle = questionTitle;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            AES aes = new AES();
            String key = Main.getEncryptionKey();
            aes.initKey(key);

            String encryptedMark = aes.encrypt(String.valueOf(mark));
            String encryptedQuestionTitle = aes.encrypt(questionTitle);
            String encryptedCorrectAnswer = aes.encrypt(String.valueOf(correctAnswer));

            //add question to database
            sql = String.format("INSERT INTO questions (mark, question_title, correct_answer, exam_id) VALUES ('%s', '%s', '%s', " + exam.getDatabaseID() + ")", encryptedMark, encryptedQuestionTitle, encryptedCorrectAnswer);
            statement.execute(sql);

            //set databaseID
            sql = "SELECT COUNT(id) FROM questions";
            databaseID = statement.executeQuery(sql).getInt(1);

            for (String answer : answers){
                //add answers to database
                sql = String.format("INSERT INTO answers (answer_title, question_id) VALUES ('%s', " + databaseID + ")", answer);
                statement.execute(sql);
            }

            DatabaseHandler.questions.add(this);

        }catch (SQLException e){
            //display error if it can't connect to the database
            System.err.println(e);
        }catch (Exception e){
            //display error if it can't encrypt
            System.err.println(e);
        }
        finally {
            //close connection
            try{
                if (connection != null){
                    connection.close();
                }
            }catch (SQLException e){
                System.err.println(e);
            }
        }
    }

    //Getters and setters

    public int getDatabaseID() {
        return databaseID;
    }

    public float getMark() {
        return mark;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public Exam getExam() {
        return exam;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
