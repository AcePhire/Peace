import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

//Template exam
public class Exam {
    private String examName;
    private LinkedList<Question> questions;
    private float totalMark;
    private Teacher teacher;

    private int databaseID;

    //Construct Exam without setting the questions
    public Exam(String examName, Teacher teacher){
        this.examName = examName;
        this.teacher = teacher;
        this.questions = new LinkedList<>();
        totalMark = 0;
        teacher.addExam(this);
    }

    //Construct Exam and set total marks
    public Exam(String examName, Teacher teacher, LinkedList<Question> questions){
        this.examName = examName;
        this.teacher = teacher;
        this.questions = questions;
        totalMark = 0;

        for(Question question1 : questions){
            totalMark += question1.getMark();
        }

        teacher.addExam(this);
    }

    //Construct Exam and set total marks when getting from the database
    public Exam(int databaseID, String examName){
        this.databaseID = databaseID;
        this.examName = examName;
        this.questions = new LinkedList<>();
        totalMark = 0;
    }

    //Add a question and add its mark to the total marks
    public void addQuestion(Question question){
        this.questions.add(question);
        totalMark += question.getMark();
    }

    //Remove a question and remove its mark from the total marks
    public void removeQuestion(Question question){
        this.questions.remove(question);
        totalMark -= question.getMark();
    }

    //Create an exam for the student to attempt
    public AttemptedExam attemptExam(Student student){
        //Check if the student attempted the exam before
        for (AttemptedExam attemptedExam : student.getAttemptedExams()) {
            if (attemptedExam.getExam() == this){
                return null;
            }
        }

        AttemptedExam attemptedExam = new AttemptedExam(this, student);
        student.addAttemptedExam(attemptedExam);
        return attemptedExam;
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add exam to database
            sql = String.format("INSERT INTO exams (exam_name, teacher_id) VALUES ('%s', " + teacher.getID() + ")", getExamName());
            statement.execute(sql);

            //set databaseID
            sql = "SELECT COUNT(id) FROM exams";
            databaseID = statement.executeQuery(sql).getInt(1);

            DatabaseHandler.exams.add(this);

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

    public String getExamName() {
        return examName;
    }

    public float getTotalMark() {
        return totalMark;
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public void setTotalMark(float totalMark) {
        this.totalMark = totalMark;
    }
}
