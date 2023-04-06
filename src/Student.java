import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class Student extends User{
    private LinkedList<AttemptedExam> attemptedExams;

    public Student(String ID, String firstName, String lastName, String hashedPassword){
        super(ID, firstName, lastName, hashedPassword);

        attemptedExams = new LinkedList<>();
    }

    public AttemptedExam getAttemptedExam(AttemptedExam exam){
        return attemptedExams.get(attemptedExams.indexOf(exam));
    }

    public void addAttemptedExam(AttemptedExam exam){
        this.attemptedExams.add(exam);
    }

    //Getters and setters

    public LinkedList<AttemptedExam> getAttemptedExams() {
        return attemptedExams;
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add student to database
            sql = String.format("INSERT INTO students (id, first_name, last_name, hashed_password) VALUES ('%s', '%s', '%s', '%s')", getID(), getFirstName(), getLastName(), getHashedPassword());
            statement.execute(sql);

            DatabaseHandler.students.add(this);

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
}
