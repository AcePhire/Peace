import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class Teacher extends User{

    private LinkedList<Exam> exams;

    public Teacher(String ID, String firstName, String lastName, String hashedPassword){
        super(ID, firstName, lastName, hashedPassword);

        exams = new LinkedList<>();
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add teacher to database
            sql = String.format("INSERT INTO teachers (id, first_name, last_name, hashed_password) VALUES ('%s', '%s', '%s', '%s')", getID(), getFirstName(), getLastName(), getHashedPassword());
            statement.execute(sql);

            DatabaseHandler.teachers.add(this);

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

    public void addExam(Exam exam){
        exams.add(exam);
    }

    public void remvoeExam(Exam exam){
        exams.remove(exam);
    }

    //Getters and setters

    public LinkedList<Exam> getExams() {
        return exams;
    }
}
