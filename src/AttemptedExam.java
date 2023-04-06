import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

//Exam attempted by the student
public class AttemptedExam{
    private Exam exam;
    private Student student;
    private LinkedList<AttemptedQuestion> questions = new LinkedList<>();

    private int databaseID;

    //Construct the exam and map the questions[Question] of the template exam[Exam] to the question[AttemptedQuestion] of the attempted exam[AttemptedExam]
    public AttemptedExam(Exam exam, Student student){
        this.exam = exam;
        this.student = student;
        //Take each question in the template exam and make an attemptedQuestion out of it
        for (Question question : exam.getQuestions()){
            AttemptedQuestion attemptedQuestion = new AttemptedQuestion(question, this);
            this.questions.add(attemptedQuestion);
        }
    }

    //Construct when getting from the database
    public AttemptedExam(int databaseID, Exam exam, LinkedList<AttemptedQuestion> attemptedQuestions){
        this.databaseID = databaseID;
        this.exam = exam;
        this.questions = attemptedQuestions;
    }

    //Answer a question by taking its number and the answer
    public void answerQuestion(int questionNumber, int answer){
        questions.get(questionNumber).answerQuestion(answer);
    }

    //Get the mark depending on if the answer is correct
    public float getMark(){
        float mark = 0;
        //Take each question and check if the answer is correct, if it is, then add to the mark to be returned
        for(AttemptedQuestion question : questions){
            if (question.checkAnswer()){
                mark += question.getBaseQuestion().getMark();
            }
        }
        return mark;
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add attempted exam to database
            sql = String.format("INSERT INTO attempted_exams (exam_id, student_id) VALUES (" + exam.getDatabaseID() +", " + student.getID() + ")");
            statement.execute(sql);

            //set databaseID
            sql = "SELECT COUNT(id) FROM attempted_exams";
            databaseID = statement.executeQuery(sql).getInt(1);

            DatabaseHandler.attemptedExams.add(this);

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

    public int getDatabaseID() {
        return databaseID;
    }

    public Student getStudent() {
        return student;
    }

    public LinkedList<AttemptedQuestion> getQuestions() {
        return questions;
    }

    public Exam getExam() {
        return exam;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
