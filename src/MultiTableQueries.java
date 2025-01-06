/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author kiami
 */
public class MultiTableQueries {
    private static Connection connection;
    private static PreparedStatement getAllClassDescriptions;
    private static PreparedStatement getScheduledStudentsbyClass;
    private static PreparedStatement getWaitlistedStudentsbyClass;
    private static ResultSet resultSet;
    
    public static ArrayList<ClassDescription> getAllClassDescriptions(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<ClassDescription> classDescriptions = new ArrayList<>();
        try
        {
            getAllClassDescriptions = connection.prepareStatement(
                    "select app.class.courseCode, description, seats from app.class, app.course where semester = ?"
                    + " and app.class.courseCode = app.course.courseCode order by app.class.courseCode\n");
            getAllClassDescriptions.setString(1, semester);
            resultSet = getAllClassDescriptions.executeQuery();
            
            while(resultSet.next())
            {
                String courseCode = resultSet.getString("coursecode");
                String description = resultSet.getString("description");
                int seats = resultSet.getInt("seats");
                classDescriptions.add(new ClassDescription(courseCode, description, seats));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return classDescriptions;
    }
    
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> scheduledStudents = new ArrayList<>();
        try
        {
            getScheduledStudentsbyClass = connection.prepareStatement(
                    "select studentid from app.schedule where semester = ? and courseCode = ? and status = ? order by timestamp");
            getScheduledStudentsbyClass.setString(1, semester);
            getScheduledStudentsbyClass.setString(2, courseCode);
            getScheduledStudentsbyClass.setString(3, "S");
            resultSet = getScheduledStudentsbyClass.executeQuery();
            
            while(resultSet.next())
            {
                String studentID = resultSet.getString("studentid");
                StudentEntry student = StudentQueries.getStudent(studentID);
                scheduledStudents.add(new StudentEntry(studentID, student.getFirstName(), student.getLastName()));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduledStudents;
    }
    
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> waitlistedStudents = new ArrayList<>();
        try
        {
            getWaitlistedStudentsbyClass = connection.prepareStatement(
                    "select studentid from app.schedule where semester = ? and courseCode = ? and status = ? order by timestamp");
            getWaitlistedStudentsbyClass.setString(1, semester);
            getWaitlistedStudentsbyClass.setString(2, courseCode);
            getWaitlistedStudentsbyClass.setString(3, "W");
            resultSet = getWaitlistedStudentsbyClass.executeQuery();
            
            while(resultSet.next())
            {
                String studentID = resultSet.getString("studentid");
                StudentEntry student = StudentQueries.getStudent(studentID);
                waitlistedStudents.add(new StudentEntry(studentID, student.getFirstName(), student.getLastName()));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return waitlistedStudents;
    }
    
}
