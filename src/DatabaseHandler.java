import java.sql.*;
import java.util.LinkedList;

public class DatabaseHandler {
    public static LinkedList<Admin> admins = new LinkedList<>();
    public static LinkedList<Teacher> teachers = new LinkedList<>();
    public static LinkedList<Student> students = new LinkedList<>();
    public static LinkedList<Exam> exams = new LinkedList<>();
    public static LinkedList<AttemptedExam> attemptedExams = new LinkedList<>();
    public static LinkedList<Question> questions = new LinkedList<>();
    public static LinkedList<AttemptedQuestion> attemptedQuestions = new LinkedList<>();

    public static void makeDatabase(){

        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //create users tables
            sql = "CREATE TABLE admins (id TEXT PRIMARY KEY, first_name TEXT, last_name TEXT, hashed_password TEXT)";
            statement.execute(sql);
            sql = "CREATE TABLE teachers (id TEXT PRIMARY KEY, first_name TEXT, last_name TEXT, hashed_password TEXT)";
            statement.execute(sql);
            sql = "CREATE TABLE students (id TEXT PRIMARY KEY, first_name TEXT, last_name TEXT, hashed_password TEXT)";
            statement.execute(sql);

            //create exam tables
            sql = "CREATE TABLE exams (id INTEGER PRIMARY KEY AUTOINCREMENT, exam_name TEXT, teacher_id INTEGER, FOREIGN KEY(teacher_id) REFERENCES teachers(id))";
            statement.execute(sql);
            sql = "CREATE TABLE attempted_exams (id INTEGER PRIMARY KEY AUTOINCREMENT, exam_id INTEGER, student_id INTEGER, FOREIGN KEY(exam_id) REFERENCES exams(id), FOREIGN KEY(student_id) REFERENCES students(id))";
            statement.execute(sql);

            //create question tables
            sql = "CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, mark TEXT, question_title TEXT, correct_answer TEXT, exam_id INTEGER, FOREIGN KEY(exam_id) REFERENCES exams(id))";
            statement.execute(sql);
            sql = "CREATE TABLE attempted_questions (id INTEGER PRIMARY KEY AUTOINCREMENT, selected_answer INTEGER, question_id INTEGER, attempted_exam_id INTEGER, FOREIGN KEY(question_id) REFERENCES questions(id), FOREIGN KEY(attempted_exam_id) REFERENCES attempted_exams(id))";
            statement.execute(sql);
            sql = "CREATE TABLE answers (id INTEGER PRIMARY KEY AUTOINCREMENT, answer_title TEXT, question_id)";
            statement.execute(sql);

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

    public static void getDatabaseData(){
        getQuestionsFromDatabase();
        getAttemptedQuestionsFromDatabase();
        getExamsFromDatabase();
        getAttemptedExamFromDatabase();
        getAdminsFromDatabase();
        getStudentsFromDatabase();
        getTeachersFromDatabase();
    }

    private static void getAdminsFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM admins";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each admin
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM admins LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                String id = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String hashedPassword = rs.getString(4);

                //make the admin object and add to the list
                Admin admin = new Admin(id, firstName, lastName, hashedPassword);
                admins.add(admin);
            }
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

    private static void getTeachersFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM teachers";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each teacher
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM teachers LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                String id = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String hashedPassword = rs.getString(4);

                //make the teacher object
                Teacher teacher = new Teacher(id, firstName, lastName, hashedPassword);

                //get the exams and add to teacher
                sql = String.format("SELECT COUNT(id) FROM exams WHERE teacher_id == '%s'", id);
                int examsRows = statement.executeQuery(sql).getInt(1);
                //go through the exams rows
                for (int j = 0; j < examsRows; j++){
                    //get the id for each
                    sql = String.format("SELECT * FROM exams WHERE teacher_id == '%s' LIMIT 1 OFFSET " + j, id);
                    int examID = statement.executeQuery(sql).getInt(1);
                    //find the exam with that id from the list
                    for (Exam exam : exams){
                        if (exam.getDatabaseID() == examID){
                            teacher.addExam(exam);
                            exam.setTeacher(teacher);
                        }
                    }
                }

                //set teacher as an admin's student account
                if (teacher.getID().toCharArray()[1] == '1'){
                    for (Admin admin : admins){
                        StringBuilder adminStringBuilder = new StringBuilder(admin.getID());
                        StringBuilder teacherStringBuilder = new StringBuilder(teacher.getID());
                        if (adminStringBuilder.substring(2) == teacherStringBuilder.substring(2)){
                            admin.setTeacherAccount(teacher);
                        }
                    }
                }

                //add to the list
                teachers.add(teacher);
            }
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

    private static void getStudentsFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM students";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each student
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM students LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                String id = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String hashedPassword = rs.getString(4);

                //make the student object
                Student student = new Student(id, firstName, lastName, hashedPassword);

                //get the attempted exams and add to student
                sql = String.format("SELECT COUNT(id) FROM attempted_exams WHERE student_id == '%s'", id);
                int attemptedExamsRows = statement.executeQuery(sql).getInt(1);
                //go through the attempted_exams rows
                for (int j = 0; j < attemptedExamsRows; j++){
                    //get the id for each
                    sql = String.format("SELECT * FROM attempted_exams WHERE student_id == '%s' LIMIT 1 OFFSET " + j, id);
                    int attemptedExamID = statement.executeQuery(sql).getInt(1);
                    //find the attempted exam with that id from the list
                    for (AttemptedExam attemptedExam : attemptedExams){
                        if (attemptedExam.getDatabaseID() == attemptedExamID){
                            student.addAttemptedExam(attemptedExam);
                            attemptedExam.setStudent(student);
                        }
                    }
                }

                //set student as an admin's student account
                if (student.getID().toCharArray()[1] == '1'){
                    for (Admin admin : admins){
                        StringBuilder adminStringBuilder = new StringBuilder(admin.getID());
                        StringBuilder studentStringBuilder = new StringBuilder(student.getID());
                        if (adminStringBuilder.substring(2) == studentStringBuilder.substring(2)){
                            admin.setStudentAccount(student);
                        }
                    }
                }

                //add to the list
                students.add(student);
            }
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

    private static void getExamsFromDatabase(){
        Exam exam;
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM exams";
            int rows = statement.executeQuery(sql).getInt(1);


            //go through each row and get the data for each exam
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM exams LIMIT 1 OFFSET " + i;
                int id = statement.executeQuery(sql).getInt(1);
                String examName = statement.executeQuery(sql).getString(2);

                //make the exam object
                exam = new Exam(id, examName);

                //get the questions and add to exam
                sql = String.format("SELECT COUNT(id) FROM questions WHERE exam_id == %s", id);
                int questionsRows = statement.executeQuery(sql).getInt(1);
                //go through the questions rows
                for (int j = 0; j < questionsRows; j++){
                    //get the id for each
                    sql = String.format("SELECT * FROM questions WHERE exam_id == %s LIMIT 1 OFFSET " + j, id);
                    int questionID = statement.executeQuery(sql).getInt(1);
                    //find the question with that id from the list
                    for (Question question : questions){
                        if (question.getDatabaseID() == questionID){
                            exam.addQuestion(question);
                            question.setExam(exam);
                        }
                    }
                }
                //add to the list
                exams.add(exam);
            }
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

    private static void getAttemptedExamFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM attempted_exams";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each attempted exam
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM attempted_exams LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                int id = rs.getInt(1);
                int examID = rs.getInt(2);

                //go through each exam to get the one with the right id
                for (Exam exam: exams) {
                    if (exam.getDatabaseID() == examID){
                        LinkedList<AttemptedQuestion> attemptedQuestionsForExam = new LinkedList<>();

                        //get the attempted questions and to put into a list of attempted questions
                        sql = String.format("SELECT COUNT(id) FROM attempted_questions WHERE attempted_exam_id == %s", id);
                        int attemptedQuestionsRows = statement.executeQuery(sql).getInt(1);
                        //go through the attempted_questions rows
                        for (int j = 0; j < attemptedQuestionsRows; j++){
                            //get the id for each
                            sql = String.format("SELECT * FROM attempted_questions WHERE attempted_exam_id == %s LIMIT 1 OFFSET " + j, id);
                            int attemptedQuestionID = statement.executeQuery(sql).getInt(1);
                            //find the attempted question with that id from the list
                            for (AttemptedQuestion attemptedQuestion : attemptedQuestions){
                                if (attemptedQuestion.getDatabaseID() == attemptedQuestionID){
                                    attemptedQuestionsForExam.add(attemptedQuestion);
                                }
                            }
                        }

                        //make the attemptedExam object and add to the list
                        AttemptedExam attemptedExam = new AttemptedExam(id, exam, attemptedQuestionsForExam);
                        attemptedExams.add(attemptedExam);

                        //get the attempted questions
                        //go through the attempted_questions rows
                        for (int j = 0; j < attemptedQuestionsRows; j++){
                            //get the id for each
                            sql = String.format("SELECT * FROM attempted_questions WHERE attempted_exam_id == %s LIMIT 1 OFFSET " + j, id);
                            int attemptedQuestionID = statement.executeQuery(sql).getInt(1);
                            //find the attempted question with that id from the list
                            for (AttemptedQuestion attemptedQuestion : attemptedQuestions){
                                if (attemptedQuestion.getDatabaseID() == attemptedQuestionID){
                                    attemptedQuestion.setAttemptedExam(attemptedExam);
                                }
                            }
                        }
                    }
                }
            }
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

    private static void getQuestionsFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM questions";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each question
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM questions LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                int id = rs.getInt(1);

                AES aes = new AES();
                String key = Main.getEncryptionKey();
                aes.initKey(key);

                String encryptedMark = rs.getString(2);
                float mark = Float.parseFloat(aes.decrypt(encryptedMark));

                String encryptedQuestionTitle = rs.getString(3);
                String questionTitle = aes.decrypt(encryptedQuestionTitle);


                String encryptedCorrectAnswer = rs.getString(4);
                int correctAnswer = Integer.parseInt(aes.decrypt(encryptedCorrectAnswer));

                LinkedList<String> answers = new LinkedList<>();

                //get the answers and to put into a list of answers
                sql = String.format("SELECT COUNT(id) FROM answers WHERE question_id == %s", id);
                int answerRows = statement.executeQuery(sql).getInt(1);
                for (int j = 0; j < answerRows; j++){
                    sql = String.format("SELECT * FROM answers WHERE question_id == %s LIMIT 1 OFFSET " + j, id);
                    answers.add(statement.executeQuery(sql).getString(2));
                }

                //make the question object and add to the list
                Question question = new Question(id, mark, questionTitle, answers.toArray(new String[answers.size()]), correctAnswer);
                questions.add(question);

            }
        }catch (SQLException e){
            //display error if it can't connect to the database
            System.err.println(e);
        }catch (Exception e){
            //display error if it can't decrypt
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

    private static void getAttemptedQuestionsFromDatabase(){
        Connection connection = null;

        //try to connect to the database
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            String sql;

            //get the number of rows and put it in an int
            sql = "SELECT COUNT(id) FROM attempted_questions";
            int rows = statement.executeQuery(sql).getInt(1);

            //go through each row and get the data for each attempted questions
            for (int i = 0; i < rows; i++){
                sql = "SELECT * FROM attempted_questions LIMIT 1 OFFSET " + i;
                ResultSet rs = statement.executeQuery(sql);
                int id = rs.getInt(1);
                int selectedAnswer = rs.getInt(2);
                int questionID = rs.getInt(3);

                //go through each question to get the one with the right id
                for (Question question: questions) {
                    if (question.getDatabaseID() == questionID) {
                        //make the attemptedQuestion object and add to the list
                        AttemptedQuestion attemptedQuestion = new AttemptedQuestion(id, question);
                        attemptedQuestion.answerQuestion(selectedAnswer);
                        attemptedQuestions.add(attemptedQuestion);
                    }
                }
            }
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
