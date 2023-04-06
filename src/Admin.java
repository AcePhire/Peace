import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin extends User{

    private Student studentAccount;
    private Teacher teacherAccount;

    public Admin(String ID, String firstName, String lastName, String hashedPassword){
        super(ID, firstName, lastName, hashedPassword);

        StringBuilder stringBuilder= new StringBuilder(ID);
        String studentID = "21" + stringBuilder.substring(2);
        String teacherID = "11" + stringBuilder.substring(2);
        studentAccount = new Student(studentID, firstName, lastName, hashedPassword);
        teacherAccount = new Teacher(teacherID, firstName, lastName, hashedPassword);
    }

    public void addToDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //add admin to databasea
            sql = String.format("INSERT INTO admins (id, first_name, last_name, hashed_password) VALUES ('%s', '%s', '%s', '%s')", getID(), getFirstName(), getLastName(), getHashedPassword());
            statement.execute(sql);

            sql = String.format("INSERT INTO students (id, first_name, last_name, hashed_password) VALUES ('%s', '%s', '%s', '%s')", studentAccount.getID(), studentAccount.getFirstName(), studentAccount.getLastName(), studentAccount.getHashedPassword());
            statement.execute(sql);

            sql = String.format("INSERT INTO teachers (id, first_name, last_name, hashed_password) VALUES ('%s', '%s', '%s', '%s')", teacherAccount.getID(), teacherAccount.getFirstName(), teacherAccount.getLastName(), teacherAccount.getHashedPassword());
            statement.execute(sql);

            DatabaseHandler.admins.add(this);
            DatabaseHandler.students.add(studentAccount);
            DatabaseHandler.teachers.add(teacherAccount);

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

    public Student getStudentAccount() {
        return studentAccount;
    }

    public Teacher getTeacherAccount() {
        return teacherAccount;
    }

    public void setStudentAccount(Student studentAccount) {
        this.studentAccount = studentAccount;
    }

    public void setTeacherAccount(Teacher teacherAccount) {
        this.teacherAccount = teacherAccount;
    }
}
