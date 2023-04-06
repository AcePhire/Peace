import UI.*;
import UI.Object;
import UI.Rectangle;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class GUIHandler {
    static int APP_WIDTH = 800, APP_HEIGHT = 600;

    public static void loginPage(App app){
        Text loginLabel = new Text("Login", 50, 400, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 400, 170, Color.decode("#d90429"), 1);

        UI.TextField idTF = new UI.TextField(300, 200, 200, 30, Color.decode("#93bad2"), 1);
        idTF.setTextSize(20);
        idTF.setFixedText("ID");
        idTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField passwordTF = new UI.TextField(300, 250, 200, 30, Color.decode("#93bad2"), 1);
        passwordTF.setAsPassword(true);
        passwordTF.setTextSize(20);
        passwordTF.setFixedText("Password");
        passwordTF.setTextColor(Color.decode("#f5f8f9"));

        ActionButton loginButton = new ActionButton(300, 350, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        loginButton.setText("Login", 18);
        loginButton.setArcs(5, 5);

        loginButton.setAction(new Action(){

            @Override
            public void action() {
                String id = idTF.getText();
                String password = passwordTF.getText();
                boolean correctCredentials = false;

                //admin ids     0[][][][][][][]
                //teachers ids  10[][][][][][]
                //student ids   20[][][][][][]
                if (id.toCharArray()[0] == '0'){
                    for (Admin admin : DatabaseHandler.admins){
                        if (admin.getID().equals(id) && checkPassword(password, admin.getHashedPassword())){
                            app.handler.removeObjects(new UI.Object[] {loginLabel, idTF, passwordTF, loginButton});
                            if (!correctCredentials){
                                app.handler.removeObject(errorLabel);
                            }
                            adminView(app, admin);
                            correctCredentials = true;
                        }
                    }
                }else if(id.toCharArray()[0] == '1' && id.toCharArray()[1] == '0'){
                    for (Teacher teacher : DatabaseHandler.teachers){
                        if (teacher.getID().equals(id) && checkPassword(password, teacher.getHashedPassword())){
                            app.handler.removeObjects(new UI.Object[] {loginLabel, idTF, passwordTF, loginButton});
                            if (!correctCredentials){
                                app.handler.removeObject(errorLabel);
                            }
                            teacherView(app, teacher);
                            correctCredentials = true;
                        }
                    }
                }else if(id.toCharArray()[0] == '2' && id.toCharArray()[1] == '0'){
                    for (Student student : DatabaseHandler.students){
                        if (student.getID().equals(id) && checkPassword(password, student.getHashedPassword())){
                            app.handler.removeObjects(new UI.Object[] {loginLabel, idTF, passwordTF, loginButton});
                            if (!correctCredentials){
                                app.handler.removeObject(errorLabel);
                            }
                            studentView(app, student);
                            correctCredentials = true;
                        }
                    }
                }

                if(!correctCredentials){
                    errorLabel.setText("Wrong ID or password");
                    if (!app.handler.objects.contains(errorLabel)) {
                        app.handler.addObject(errorLabel);
                    }
                }
            }
        });

        //
        app.handler.addObjects(new UI.Object[] {loginLabel, idTF, passwordTF, loginButton});
    }

    public static void adminView(App app, Admin admin){
        Text welcomeLabel = new Text(String.format("Welcome back, %s %s", admin.getFirstName(), admin.getLastName()),
                28, 420, 40, Color.decode("#93bad2"), 1);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton testStudentButton = new ActionButton(20, 30, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        testStudentButton.setText("Test Student", 16);

        ActionButton addStudentButton = new ActionButton(20, 90, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addStudentButton.setText("Add Student", 16);

        ActionButton testTeacherButton = new ActionButton(20, 150, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        testTeacherButton.setText("Test Teacher", 16);

        ActionButton addTeacherButton = new ActionButton(20, 210, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addTeacherButton.setText("Add Teacher", 16);

        ActionButton addAdminButton = new ActionButton(20, 270, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addAdminButton.setText("Add Admin", 16);

        ActionButton logoutButton = new ActionButton(APP_WIDTH-100, 30, 80, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        logoutButton.setText("Logout", 16);
        logoutButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton, logoutButton});

                loginPage(app);
            }
        });

        addStudentButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton, logoutButton});

                addStudentView(app, admin);
            }
        });

        addTeacherButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton, logoutButton});

                addTeacherView(app, admin);
            }
        });

        addAdminButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton,logoutButton});

                addAdminView(app, admin);
            }
        });

        testStudentButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton,logoutButton});

                studentView(app, admin.getStudentAccount());
            }
        });

        testTeacherButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton,logoutButton});

                teacherView(app, admin.getTeacherAccount());
            }
        });

        app.handler.addObjects(new UI.Object[] {welcomeLabel, sidePanel, testStudentButton, addStudentButton, testTeacherButton, addTeacherButton, addAdminButton, logoutButton});
    }

    public static void addStudentView(App app, Admin admin){
        Text addStudentLabel = new Text("Add Student", 50, 500, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 500, 170, Color.decode("#d90429"), 1);

        Text idLabel = new Text("20", 30, 380, 217, Color.decode("#93bad2"), 1);

        UI.TextField idTF = new UI.TextField(400, 200, 200, 30, Color.decode("#93bad2"), 1);
        idTF.setTextSize(20);
        idTF.setFixedText("ID [6 digits]");
        idTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField firstNamedTF = new UI.TextField(400, 250, 200, 30, Color.decode("#93bad2"), 1);
        firstNamedTF.setTextSize(20);
        firstNamedTF.setFixedText("First Name");
        firstNamedTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField lastNameTF = new UI.TextField(400, 300, 200, 30, Color.decode("#93bad2"), 1);
        lastNameTF.setTextSize(20);
        lastNameTF.setFixedText("Last Name");
        lastNameTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField passwordTF = new UI.TextField(400, 350, 200, 30, Color.decode("#93bad2"), 1);
        passwordTF.setAsPassword(true);
        passwordTF.setTextSize(20);
        passwordTF.setFixedText("Password");
        passwordTF.setTextColor(Color.decode("#f5f8f9"));

        ActionButton addButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addButton.setText("Add", 18);
        addButton.setArcs(5, 5);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);
        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {addStudentLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});
                adminView(app, admin);
            }
        });

        addButton.setAction(new Action(){
            @Override
            public void action() {

                if (idTF.getText().length() > 6 || idTF.getText().length() < 6 || firstNamedTF.getText().equals("") || lastNameTF.getText().equals("") || passwordTF.getText().equals("")){
                    errorLabel.setText("Invalid Input");
                    if (!app.handler.objects.contains(errorLabel)){
                        app.handler.addObject(errorLabel);
                    }
                }else {
                    boolean idUsed = false;

                    for (Student s : DatabaseHandler.students){
                        if (s.getID().equals("20" + idTF.getText())){
                            idUsed = true;
                        }
                    }

                    if(idUsed){
                        errorLabel.setText("ID already used");
                        if (!app.handler.objects.contains(errorLabel)){
                            app.handler.addObject(errorLabel);
                        }
                    }else{
                        Student newStudent = new Student("20" + idTF.getText(), firstNamedTF.getText(), lastNameTF.getText(), hashPassword(passwordTF.getText()));
                        newStudent.addToDatabase();

                        app.handler.removeObjects(new UI.Object[] {addStudentLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});
                        adminView(app, admin);
                    }
                }
            }
        });

        app.handler.addObjects(new UI.Object[] {addStudentLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton});
    }

    public static void addTeacherView(App app, Admin admin){
        Text addTeacherLabel = new Text("Add Teacher", 50, 500, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 500, 170, Color.decode("#d90429"), 1);

        Text idLabel = new Text("10", 30, 380, 217, Color.decode("#93bad2"), 1);

        UI.TextField idTF = new UI.TextField(400, 200, 200, 30, Color.decode("#93bad2"), 1);
        idTF.setTextSize(20);
        idTF.setFixedText("ID [6 digits]");
        idTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField firstNamedTF = new UI.TextField(400, 250, 200, 30, Color.decode("#93bad2"), 1);
        firstNamedTF.setTextSize(20);
        firstNamedTF.setFixedText("First Name");
        firstNamedTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField lastNameTF = new UI.TextField(400, 300, 200, 30, Color.decode("#93bad2"), 1);
        lastNameTF.setTextSize(20);
        lastNameTF.setFixedText("Last Name");
        lastNameTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField passwordTF = new UI.TextField(400, 350, 200, 30, Color.decode("#93bad2"), 1);
        passwordTF.setAsPassword(true);
        passwordTF.setTextSize(20);
        passwordTF.setFixedText("Password");
        passwordTF.setTextColor(Color.decode("#f5f8f9"));

        ActionButton addButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addButton.setText("Add", 18);
        addButton.setArcs(5, 5);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);
        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {addTeacherLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});

                adminView(app, admin);
            }
        });

        addButton.setAction(new Action(){
            @Override
            public void action() {
                if (idTF.getText().length() > 6 || idTF.getText().length() < 6 || firstNamedTF.getText().equals("") || lastNameTF.getText().equals("") || passwordTF.getText().equals("")){
                    errorLabel.setText("Invalid Input");
                    if (!app.handler.objects.contains(errorLabel)){
                        app.handler.addObject(errorLabel);
                    }
                }else{
                    boolean idUsed = false;

                    for (Teacher t : DatabaseHandler.teachers){
                        if (t.getID().equals("10" + idTF.getText())){
                            idUsed = true;
                        }
                    }

                    if(idUsed){
                        errorLabel.setText("ID already used");
                        if (!app.handler.objects.contains(errorLabel)){
                            app.handler.addObject(errorLabel);
                        }
                    }else{
                        Teacher newTeacher = new Teacher("10" + idTF.getText(), firstNamedTF.getText(), lastNameTF.getText(), hashPassword(passwordTF.getText()));
                        newTeacher.addToDatabase();

                        app.handler.removeObjects(new UI.Object[] {addTeacherLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});
                        adminView(app, admin);
                    }
                }
            }
        });

        app.handler.addObjects(new UI.Object[] {addTeacherLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton});
    }

    public static void addAdminView(App app, Admin admin){
        Text addAdminLabel = new Text("Add Admin", 50, 500, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 500, 170, Color.decode("#d90429"), 1);

        Text idLabel = new Text("0", 30, 380, 217, Color.decode("#93bad2"), 1);

        UI.TextField idTF = new UI.TextField(400, 200, 200, 30, Color.decode("#93bad2"), 1);
        idTF.setTextSize(20);
        idTF.setFixedText("ID [7 digits]");
        idTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField firstNamedTF = new UI.TextField(400, 250, 200, 30, Color.decode("#93bad2"), 1);
        firstNamedTF.setTextSize(20);
        firstNamedTF.setFixedText("First Name");
        firstNamedTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField lastNameTF = new UI.TextField(400, 300, 200, 30, Color.decode("#93bad2"), 1);
        lastNameTF.setTextSize(20);
        lastNameTF.setFixedText("Last Name");
        lastNameTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField passwordTF = new UI.TextField(400, 350, 200, 30, Color.decode("#93bad2"), 1);
        passwordTF.setAsPassword(true);
        passwordTF.setTextSize(20);
        passwordTF.setFixedText("Password");
        passwordTF.setTextColor(Color.decode("#f5f8f9"));

        ActionButton addButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addButton.setText("Add", 18);
        addButton.setArcs(5, 5);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);
        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {addAdminLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});

                adminView(app, admin);
            }
        });

        addButton.setAction(new Action(){
            @Override
            public void action() {
                if (idTF.getText().length() > 7 || idTF.getText().length() < 7 || firstNamedTF.getText().equals("") || lastNameTF.getText().equals("") || passwordTF.getText().equals("")){
                    errorLabel.setText("Invalid Input");
                    if (!app.handler.objects.contains(errorLabel)){
                        app.handler.addObject(errorLabel);
                    }
                }else{
                    boolean idUsed = false;

                    for (Admin a : DatabaseHandler.admins){
                        if (a.getID().equals("0" + idTF.getText())){
                            idUsed = true;
                        }
                    }

                    if(idUsed){
                        errorLabel.setText("ID already used");
                        if (!app.handler.objects.contains(errorLabel)){
                            app.handler.addObject(errorLabel);
                        }
                    }else{
                        Admin newAdmin = new Admin("0" + idTF.getText(), firstNamedTF.getText(), lastNameTF.getText(), hashPassword(passwordTF.getText()));
                        newAdmin.addToDatabase();

                        app.handler.removeObjects(new UI.Object[] {addAdminLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton, errorLabel});
                        adminView(app, admin);
                    }
                }
            }
        });

        app.handler.addObjects(new UI.Object[] {addAdminLabel, idLabel, idTF, firstNamedTF, lastNameTF, passwordTF, addButton, sidePanel, backButton});
    }

    public static void teacherView(App app, Teacher teacher){
        Text welcomeLabel = new Text(String.format("Welcome back, %s %s", teacher.getFirstName(), teacher.getLastName()),
                28, 420, 40, Color.decode("#93bad2"), 1);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton createExamButton = new ActionButton(20, 90, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        createExamButton.setText("Create Exam", 16);

        ActionButton listExamsButton = new ActionButton(20, 30, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        listExamsButton.setText("List Exams", 16);

        ActionButton logoutButton = new ActionButton(APP_WIDTH-100, 30, 80, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        logoutButton.setText("Logout", 16);
        logoutButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, createExamButton, listExamsButton, logoutButton});

                loginPage(app);
            }
        });

        createExamButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, createExamButton, listExamsButton, logoutButton});

                createExamView(app, teacher);
            }
        });

        listExamsButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, createExamButton, listExamsButton, logoutButton});

                listExamsView(app, teacher);
            }
        });

        app.handler.addObjects(new UI.Object[] {welcomeLabel, sidePanel, createExamButton, listExamsButton, logoutButton});
    }

    public static void createExamView(App app, Teacher teacher){
        Text createExamLabel = new Text("Create Exam", 50, 500, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 500, 170, Color.decode("#d90429"), 1);

        UI.TextField examNameTF = new UI.TextField(400, 200, 200, 30, Color.decode("#93bad2"), 1);
        examNameTF.setTextSize(20);
        examNameTF.setFixedText("Exam Name");
        examNameTF.setTextColor(Color.decode("#f5f8f9"));

        ActionButton addQuestionsButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addQuestionsButton.setText("Add Questions", 18);
        addQuestionsButton.setArcs(5, 5);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);

        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {createExamLabel, examNameTF, addQuestionsButton, sidePanel, backButton, errorLabel});
                teacherView(app, teacher);
            }
        });

        addQuestionsButton.setAction(new Action(){
            @Override
            public void action() {
                String examName = examNameTF.getText();

                if (examName.equals("")) {
                    errorLabel.setText("Invalid Input");
                    if (!app.handler.objects.contains(errorLabel)) {
                        app.handler.addObject(errorLabel);
                    }
                }else{
                    Exam exam = new Exam(examName, teacher);
                    exam.addToDatabase();

                    app.handler.removeObjects(new UI.Object[] {createExamLabel, examNameTF, addQuestionsButton, sidePanel, backButton, errorLabel});
                    addQuestionsView(app, teacher, exam);
                }
            }
        });

        app.handler.addObjects(new UI.Object[] {createExamLabel, examNameTF, addQuestionsButton, sidePanel, backButton});
    }

    public static void addQuestionsView(App app, Teacher teacher, Exam exam){
        Text createExamLabel = new Text("Add Questions", 50, 400, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 400, 170, Color.decode("#d90429"), 1);

        UI.TextField questionTF = new UI.TextField(30, 200, 200, 30, Color.decode("#93bad2"), 1);
        questionTF.setTextSize(20);
        questionTF.setFixedText("Question");
        questionTF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField answer1TF = new UI.TextField(30, 250, 200, 30, Color.decode("#93bad2"), 1);
        answer1TF.setTextSize(20);
        answer1TF.setFixedText("Answer 1");
        answer1TF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField answer2TF = new UI.TextField(30, 300, 200, 30, Color.decode("#93bad2"), 1);
        answer2TF.setTextSize(20);
        answer2TF.setFixedText("Answer 2");
        answer2TF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField answer3TF = new UI.TextField(30, 350, 200, 30, Color.decode("#93bad2"), 1);
        answer3TF.setTextSize(20);
        answer3TF.setFixedText("Answer 3");
        answer3TF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField answer4TF = new UI.TextField(30, 400, 200, 30, Color.decode("#93bad2"), 1);
        answer4TF.setTextSize(20);
        answer4TF.setFixedText("Answer 4");
        answer4TF.setTextColor(Color.decode("#f5f8f9"));

        UI.TextField markTF = new UI.TextField(30, 450, 200, 30, Color.decode("#93bad2"), 1);
        markTF.setTextSize(20);
        markTF.setFixedText("Mark");
        markTF.setTextColor(Color.decode("#f5f8f9"));

        Text correctAnswerLabel = new Text("Correct Answer", 20, APP_WIDTH/2, 210, Color.decode("#93bad2"), 1);

        DropdownList correctAnswerList = new DropdownList(new String[]{"1", "2", "3", "4"}, APP_WIDTH/2-75, 230, 150, 40, Color.decode("#93bad2"), Color.decode("#f5f8f9"), 1);

        ActionButton addQuestionButton = new ActionButton(APP_WIDTH-450, 540, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        addQuestionButton.setText("Add Question", 18);
        addQuestionButton.setArcs(5, 5);
        addQuestionButton.setAction(new Action(){
            @Override
            public void action() {
                String questionTitle = questionTF.getText();
                String answer1 = answer1TF.getText();
                String answer2 = answer2TF.getText();
                String answer3 = answer3TF.getText();
                String answer4 = answer4TF.getText();
                int correctAnswer = correctAnswerList.getSelectedItemIndex()+1;
                float mark = 0;

                if (questionTitle.equals("") || answer1.equals("") || answer2.equals("") || answer3.equals("") || answer4.equals("") || correctAnswerList.getSelectedItem() == null ){
                    errorLabel.setText("Invalid Input");
                    if (!app.handler.objects.contains(errorLabel)){
                        app.handler.addObject(errorLabel);
                    }
                }else{
                    try{
                        mark = Float.parseFloat(markTF.getText());

                        Question question = new Question(mark, questionTitle, new String[]{answer1, answer2, answer3, answer4}, correctAnswer, exam);
                        exam.addQuestion(question);
                        question.addToDatabase();

                        questionTF.clearText();
                        answer1TF.clearText();
                        answer2TF.clearText();
                        answer3TF.clearText();
                        answer4TF.clearText();
                        markTF.clearText();
                        correctAnswerList.unselect();
                    }catch (NumberFormatException e){
                        errorLabel.setText("Mark can only be a number");
                        if (!app.handler.objects.contains(errorLabel)){
                            app.handler.addObject(errorLabel);
                        }
                    }
                }
            }
        });

        ActionButton finishAddingButton = new ActionButton(APP_WIDTH-230, 540, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        finishAddingButton.setText("Finish Adding Questions", 16);

        finishAddingButton.setAction(new Action(){
            @Override
            public void action() {
                confirmBox(app, "Are you done with making the exam?", new Action(){
                    @Override
                    public void action() {
                        app.handler.removeObjects(new UI.Object[] {createExamLabel, questionTF, answer1TF, answer2TF, answer3TF, answer4TF, markTF, correctAnswerLabel, correctAnswerList, addQuestionButton, finishAddingButton, errorLabel});
                        teacherView(app, teacher);
                    }
                });
            }
        });

        app.handler.addObjects(new UI.Object[] {createExamLabel, questionTF, answer1TF, answer2TF, answer3TF, answer4TF, markTF, correctAnswerLabel, correctAnswerList, addQuestionButton, finishAddingButton});
    }

    public static void listExamsView(App app, Teacher teacher){
        Text chooseExamLabel = new Text("Choose Exam", 50, 500, 100, Color.decode("#93bad2"), 1);

        int numberOfExam = teacher.getExams().size();
        String[] examNames = new String[numberOfExam];
        for (int i = 0; i < numberOfExam; i++){
            examNames[i] = teacher.getExams().get(i).getExamName();
        }

        ActionButton viewExamButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        viewExamButton.setText("View Exam", 18);
        viewExamButton.setArcs(5, 5);

        DropdownList examsList = new DropdownList(examNames, APP_WIDTH/2, 200, 200, 40,Color.decode("#93bad2"), Color.decode("#f5f8f9"), 1);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);

        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {chooseExamLabel, viewExamButton, examsList, sidePanel, backButton});
                teacherView(app, teacher);
            }
        });

        viewExamButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {chooseExamLabel, viewExamButton, examsList, sidePanel, backButton});
                Exam exam = teacher.getExams().get(examsList.getSelectedItemIndex());
                viewExamView(app, teacher, exam);
            }
        });

        app.handler.addObjects(new UI.Object[] {chooseExamLabel, viewExamButton, examsList, sidePanel, backButton});

    }

    public static void viewExamView(App app, Teacher teacher, Exam exam){
        Text examNameLabel = new Text(exam.getExamName(), 50, 400, 100, Color.decode("#93bad2"), 1);

        LinkedList<UI.Object> questionsLabels = new LinkedList<>();
        for (int i = 0; i < exam.getQuestions().size(); i++){
            Question question = exam.getQuestions().get(i);

            Text questionLabel = new Text(i+1 + ") " + question.getQuestionTitle(), 20, 0, 200+(i*50), Color.decode("#93bad2"), 1);
            questionLabel.setX((questionLabel.getW()/2)+30);
            questionsLabels.add(questionLabel);
        }

        ActionButton viewStudentGradesButton = new ActionButton(APP_WIDTH-450, 540, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        viewStudentGradesButton.setText("View Student Grades", 18);
        viewStudentGradesButton.setArcs(5, 5);

        ActionButton finishViewingButton = new ActionButton(APP_WIDTH-230, 540, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        finishViewingButton.setText("Finish Viewing Exam", 16);

        finishViewingButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {examNameLabel, viewStudentGradesButton, finishViewingButton});
                app.handler.removeObjects(questionsLabels);
                listExamsView(app, teacher);
            }
        });

        viewStudentGradesButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {examNameLabel, viewStudentGradesButton, finishViewingButton});
                app.handler.removeObjects(questionsLabels);
                viewStudentGradesViews(app, teacher, exam);
            }
        });

        app.handler.addObjects(new UI.Object[] {examNameLabel, viewStudentGradesButton, finishViewingButton});
        app.handler.addObjects(questionsLabels);
    }

    public static void viewStudentGradesViews(App app, Teacher teacher, Exam exam){
        LinkedList<AttemptedExam> attemptedExams = new LinkedList<>();

        for (Student student : DatabaseHandler.students){
            for (AttemptedExam attemptedExam : student.getAttemptedExams()){
                if (attemptedExam.getExam() == exam){
                    attemptedExams.add(attemptedExam);
                }
            }
        }

        LinkedList<UI.Object> attemptedExamsLabels = new LinkedList<>();
        for (int i = 0; i < attemptedExams.size(); i++){
            AttemptedExam attemptedExam = attemptedExams.get(i);
            Student student = attemptedExam.getStudent();
            String studentInfo = student.getFirstName() + " " + student.getLastName() + " | " + student.getID();

            Text attemptedExamLabel = new Text(i + ") " + studentInfo + ":     " + attemptedExam.getMark() + "/" + attemptedExam.getExam().getTotalMark(), 20, 0, 100+(i*50), Color.decode("#93bad2"), 1);
            attemptedExamLabel.setX((attemptedExamLabel.getW()/2)+30);
            attemptedExamsLabels.add(attemptedExamLabel);
        }

        ActionButton finishViewingButton = new ActionButton(APP_WIDTH-230, 30, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        finishViewingButton.setText("Finish Viewing Grades", 16);

        finishViewingButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {finishViewingButton});
                app.handler.removeObjects(attemptedExamsLabels);
                viewExamView(app, teacher, exam);
            }
        });

        app.handler.addObjects(new UI.Object[] {finishViewingButton});
        app.handler.addObjects(attemptedExamsLabels);
    }

    public static void studentView(App app, Student student){
        Text welcomeLabel = new Text(String.format("Welcome back, %s %s", student.getFirstName(), student.getLastName()),
                28, 420, 40, Color.decode("#93bad2"), 1);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton checkExamsButton = new ActionButton(20, 30, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        checkExamsButton.setText("Check Exams", 16);

        ActionButton checkMarksButton = new ActionButton(20, 90, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        checkMarksButton.setText("Check Marks", 16);

        ActionButton logoutButton = new ActionButton(APP_WIDTH-100, 30, 80, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        logoutButton.setText("Logout", 16);
        logoutButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, checkExamsButton, checkMarksButton, logoutButton});
                loginPage(app);
            }
        });

        checkExamsButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, checkExamsButton, checkMarksButton, logoutButton});
                checkExamView(app, student);
            }
        });

        checkMarksButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {welcomeLabel, sidePanel, checkExamsButton, checkMarksButton, logoutButton});
                checkMarksView(app, student);
            }
        });

        app.handler.addObjects(new UI.Object[] {welcomeLabel, sidePanel, checkExamsButton, checkMarksButton, logoutButton});


    }

    public static void checkExamView(App app, Student student){
        Text chooseExamLabel = new Text("Choose Exam", 50, 500, 100, Color.decode("#93bad2"), 1);

        Text errorLabel = new Text("", 30, 500, 170, Color.decode("#d90429"), 1);

        int numberOfExam = DatabaseHandler.exams.size();
        String[] examNames = new String[numberOfExam];
        for (int i = 0; i < numberOfExam; i++){
            examNames[i] = DatabaseHandler.exams.get(i).getExamName();
        }

        DropdownList examsList = new DropdownList(examNames, APP_WIDTH/2, 200, 200, 40,Color.decode("#93bad2"), Color.decode("#f5f8f9"), 1);

        ActionButton startExamButton = new ActionButton(400, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        startExamButton.setText("Start Exam", 18);
        startExamButton.setArcs(5, 5);

        UI.Rectangle sidePanel = new UI.Rectangle(0, 0, 180, APP_HEIGHT, Color.decode("#88889e"), 0);

        ActionButton backButton = new ActionButton(20, 540, 130, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        backButton.setText("Back", 16);

        backButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {chooseExamLabel, startExamButton, examsList, sidePanel, backButton, errorLabel});
                studentView(app, student);
            }
        });

        startExamButton.setAction(new Action(){
            @Override
            public void action() {
                Exam exam = DatabaseHandler.exams.get(examsList.getSelectedItemIndex());
                AttemptedExam attemptedExam = exam.attemptExam(student);
                if (attemptedExam == null){
                    errorLabel.setText("Exam Already Attempted");
                    if (!app.handler.objects.contains(errorLabel)){
                        app.handler.addObject(errorLabel);
                    }
                }else{
                    attemptedExam.addToDatabase();
                    app.handler.removeObjects(new UI.Object[] {chooseExamLabel, startExamButton, examsList, sidePanel, backButton, errorLabel});
                    attemptQuestionView(app, student, attemptedExam, 0);
                }
            }
        });

        app.handler.addObjects(new UI.Object[] {chooseExamLabel, startExamButton, examsList, sidePanel, backButton});
    }

    public static void attemptQuestionView(App app, Student student, AttemptedExam attemptedExam, int questionNumber){
        AttemptedQuestion attemptedQuestion = attemptedExam.getQuestions().get(questionNumber);
        Text questionLabel = new Text(attemptedQuestion.getBaseQuestion().getQuestionTitle(), 40, 400, 100, Color.decode("#93bad2"), 1);

        String[] answers = attemptedQuestion.getBaseQuestion().getAnswers();

        DropdownList answerList = new DropdownList(answers, (APP_WIDTH/2)-100, 200, 200, 40,Color.decode("#93bad2"), Color.decode("#f5f8f9"), 1);

        ActionButton nextQuestionButton = new ActionButton(300, 450, 200, 50, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        nextQuestionButton.setArcs(5, 5);
        if (questionNumber == attemptedExam.getQuestions().size()-1){
            nextQuestionButton.setText("Finish Exam", 18);
            nextQuestionButton.setAction(new Action(){
                @Override
                public void action() {
                    app.handler.removeObjects(new UI.Object[]{questionLabel, answerList, nextQuestionButton});
                    attemptedQuestion.answerQuestion(answerList.getSelectedItemIndex()+1);
                    attemptedQuestion.addToDatabase();
                    studentView(app, student);
                }
            });
        }else {
            nextQuestionButton.setText("Next Question", 18);
            nextQuestionButton.setAction(new Action(){
                @Override
                public void action() {
                    app.handler.removeObjects(new UI.Object[]{questionLabel, answerList, nextQuestionButton});
                    attemptedQuestion.answerQuestion(answerList.getSelectedItemIndex()+1);
                    attemptedQuestion.addToDatabase();
                    attemptQuestionView(app, student, attemptedExam, questionNumber + 1);
                }
            });
        }

        app.handler.addObjects(new UI.Object[]{questionLabel, answerList, nextQuestionButton});
    }

    public static void checkMarksView(App app, Student student){
        LinkedList<UI.Object> attemptedExamsLabels = new LinkedList<>();
        for (int i = 0; i < student.getAttemptedExams().size(); i++){
            AttemptedExam attemptedExam = student.getAttemptedExams().get(i);

            Text attemptedExamLabel = new Text(i+1 + ") " + attemptedExam.getExam().getExamName() + ":     " + attemptedExam.getMark() + "/"+ attemptedExam.getExam().getTotalMark(), 20, 0, 100+(i*50), Color.decode("#93bad2"), 1);
            attemptedExamLabel.setX((attemptedExamLabel.getW()/2)+30);
            attemptedExamsLabels.add(attemptedExamLabel);
        }

        ActionButton finishViewingButton = new ActionButton(APP_WIDTH-230, 540, 200, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 1);
        finishViewingButton.setText("Finish Viewing Marks", 16);

        finishViewingButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {finishViewingButton});
                app.handler.removeObjects(attemptedExamsLabels);
                studentView(app, student);
            }
        });

        app.handler.addObjects(new UI.Object[] {finishViewingButton});
        app.handler.addObjects(attemptedExamsLabels);
    }

    public static void confirmBox(App app, String message, Action action){
        UI.Rectangle confirmBox = new UI.Rectangle((APP_WIDTH/2)-175, (APP_HEIGHT/2)-75, 350, 150, Color.decode("#88889e"), 3);
        confirmBox.setArcs(10, 10);
        UI.Rectangle confirmBorder = new Rectangle((APP_WIDTH/2)-180, (APP_HEIGHT/2)-80, 360, 160, Color.decode("#f5f8f9"), 2);
        confirmBorder.setArcs(10, 10);

        Text confirmLabel = new Text(message, 20, (APP_WIDTH/2), (APP_HEIGHT/2)-50, Color.decode("#d90429"), 4);

        ActionButton backConfirmYesButton = new ActionButton((APP_WIDTH/2)-70, (APP_HEIGHT/2)+20, 65, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 4);
        backConfirmYesButton.setText("Yes", 16);

        ActionButton backConfirmNoButton = new ActionButton((APP_WIDTH/2)+10, (APP_HEIGHT/2)+20, 65, 40, Color.decode("#f5f8f9"), Color.decode("#93bad2"), 4);
        backConfirmNoButton.setText("No", 16);

        backConfirmYesButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {confirmBox, confirmBorder, confirmLabel, backConfirmYesButton, backConfirmNoButton});
                action.action();
            }
        });

        backConfirmNoButton.setAction(new Action(){
            @Override
            public void action() {
                app.handler.removeObjects(new UI.Object[] {confirmBox, confirmBorder, confirmLabel, backConfirmYesButton, backConfirmNoButton});
            }
        });

        app.handler.addObjects(new Object[] {confirmBox, confirmBorder, confirmLabel, backConfirmYesButton, backConfirmNoButton});
    }

    public static String hashPassword(String password) {
        try {
            return Hash.getSHA(password);
        }catch (NoSuchAlgorithmException e){
            System.err.println(e);
            return password;
        }
    }

    public static boolean checkPassword(String password, String hashedPassword){
        if (hashPassword(password).equals(hashedPassword)){
            return true;
        }else{
            return false;
        }
    }
}
