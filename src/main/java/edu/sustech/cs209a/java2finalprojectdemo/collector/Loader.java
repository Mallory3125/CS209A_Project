package edu.sustech.cs209a.java2finalprojectdemo.collector;

import com.alibaba.fastjson2.annotation.JSONField;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Answer;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Comment;
import edu.sustech.cs209a.java2finalprojectdemo.domain.Question;


import java.beans.Transient;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static edu.sustech.cs209a.java2finalprojectdemo.collector.DataCollector.*;
public class Loader {
    private static Connection con = null;
    private static Statement stmt = null;

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        openDB(prop);
        //!!!only create the table in first loop
//      createTable();
        //change the page parameters in datacollector
        List<Question> questions = collectQuestions();
        insertDataToDatabase(questions,"questions");
        System.out.println(questions.size()+"question done");

        List<Answer> answers = collectAnswers(questions);
        insertDataToDatabase(answers,"answers");
        System.out.println("answer done");

        List<Comment> comments = collectCommentsFromQuestion(questions);
        insertDataToDatabase(comments,"comments");

        List<Comment> comments1 = collectCommentsFromAnswer(answers);
        insertDataToDatabase(comments1,"comments");
        System.out.println("comment done");

        insertTag(questions);
        System.out.println("tag done");

        closeDB();
    }

    public static List<String> getColumnName(Field[] fields) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++)  {
            Field field = fields[i];
            String coloum = field.getName();
            if (i==0){
                coloum = "id";
            }
            else if (field.isAnnotationPresent(JSONField.class) && !field.isAnnotationPresent(Transient.class)) {
                JSONField jsonFieldAnnotation = field.getAnnotation(JSONField.class);
                String name = jsonFieldAnnotation.name();
                if (name.isEmpty()) continue;
                coloum = name;
            }
            stringList.add(coloum);
        }
        return stringList;
    }

    public static void insertTag(List<Question> questions) {
        String insertTag = "INSERT INTO tags (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        String insertRelation = "INSERT INTO question_tags (tag_name, question_id) VALUES (?, ?)";

        try (PreparedStatement insertTagStatement = con.prepareStatement(insertTag);
             PreparedStatement insertRelationStatement = con.prepareStatement(insertRelation)) {

            for (Question question : questions) {
                List<String> tags = question.getTags();
                for (String tag : tags) {
                    // Insert tag with ON CONFLICT DO NOTHING
                    insertTagStatement.setObject(1, tag);
                    insertTagStatement.executeUpdate();

                    // Insert relation between question and tag
                    insertRelationStatement.setObject(1, tag);
                    insertRelationStatement.setObject(2, question.getId());
                    insertRelationStatement.executeUpdate();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> void insertDataToDatabase(List<T> data, String tableName)  {
        T item1 = data.get(0);
        // 构建插入语句
        String insertQuery = "INSERT INTO " + tableName +" (";
        // 获取实体类的字段
        Field[] fields = item1.getClass().getDeclaredFields();
        List<String> columnsList = getColumnName(fields);

        for (String colum:columnsList) {
            insertQuery+= colum+", ";
        }
        insertQuery = insertQuery.substring(0, insertQuery.length() - 2); // 移除最后的逗号
        insertQuery += ")";
        insertQuery += " VALUES (";
        for (int i = 0; i < columnsList.size(); i++) {
            insertQuery += "?, ";
        }
        insertQuery = insertQuery.substring(0, insertQuery.length() - 2); // 移除最后的逗号
        insertQuery += ")";

        try {
            for (int j = 0; j < data.size(); j++) {
                T item = data.get(j);

                // 使用PreparedStatement设置参数
                PreparedStatement preparedStatement = con.prepareStatement(insertQuery);

                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(item);
                    if(value==null || value.getClass().equals(ArrayList.class)) {
                        continue;
                    }
                    preparedStatement.setObject(i+1 , value);
                }
                //执行插入操作
                preparedStatement.executeUpdate();

//                if (j%100==0) System.out.println(tableName+": "+j);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void createTable(){
        if (con!=null){
            try {
                String projectRoot = System.getProperty("user.dir");
                Statement statement = con.createStatement();
                String sqlFilePath = projectRoot+"/src/main/resources/cs209a_create.sql";
                BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath));
                String line;
                StringBuilder query = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    query.append(line).append(" ");
                    if (line.endsWith(";")) {
                        // 执行每个SQL语句
                        statement.execute(query.toString());
                        query.setLength(0); // 清空StringBuilder
                    }
                }
                System.out.println("create tables done");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
    private static Properties loadDBUser() {
        String projectRoot = System.getProperty("user.dir");
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(projectRoot+"/src/main/resources/dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }
    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");

        try {
            con = DriverManager.getConnection(url, prop);
            con.setSchema(prop.getProperty("schema"));// choose the schema, default is public
            if (con != null) {
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") +" scheme "+prop.getProperty("schema")+ " as " + prop.getProperty("user"));
                stmt = con.createStatement();
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
                System.out.println("closed database");
            } catch (Exception ignored) {
            }
        }
    }
}

