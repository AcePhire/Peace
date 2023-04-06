import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AttemptedQuestion{
    private int selectedAnswer = 0;

    private Question question;

    private AttemptedExam attemptedExam;

    private int databaseID;

    //Construct AttemptedQuestion
    public AttemptedQuestion(Question question, AttemptedExam attemptedExam) {
        this.question = question;
        this.attemptedExam = attemptedExam;
    }

    //Construct AttemptedQuestion when getting from the database
    public AttemptedQuestion(int databaseID, Question question) {
        this.databaseID = databaseID;
        this.question = question;
    }

    //Check if the answer of the question is correct
    public boolean checkAnswer(){
        if (selectedAnswer == question.getCorrectAnswer()){
            return true;
        }else{
            return false;
        }
    }

    //Answer the question
    public void answerQuestion(int selectedAnswer){
        this.selectedAnswer = selectedAnswer;
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add attempted question to database
            sql = String.format("INSERT INTO attempted_questions (selected_answer, question_id, attempted_exam_id) VALUES (" + selectedAnswer + ", " + question.getDatabaseID() + ", " + attemptedExam.getDatabaseID() + ")");
            statement.execute(sql);

            //set databaseID
            sql = "SELECT COUNT(id) FROM attempted_questions";
            databaseID = statement.executeQuery(sql).getInt(1);

            DatabaseHandler.attemptedQuestions.add(this);

        }catch (SQLException e){
            //display error if it can't connect to the database
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

    public AttemptedExam getAttemptedExam() {
        return attemptedExam;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public Question getBaseQuestion(){
        return question;
    }

    public void setAttemptedExam(AttemptedExam attemptedExam) {
        this.attemptedExam = attemptedExam;
    }
}
