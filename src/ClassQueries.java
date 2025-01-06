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
 * @author Negein
 */
public class ClassQueries {
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static PreparedStatement dropClassSchedule;
    private static ResultSet resultSet;
   
    public static void addClass(ClassEntry classEntry)
    {
        connection = DBConnection.getConnection();
        try
        {
            addClass = connection.prepareStatement("insert into app.class (semester, coursecode, seats) values (?, ?, ?)");
            addClass.setString(1, classEntry.getSemester());
            addClass.setString(2, classEntry.getCourseCode());
            addClass.setInt(3, classEntry.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
     public static ArrayList<String> getAllCourseCodes(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select coursecode from app.class where semester = ? order by coursecode");
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                String courseCode = resultSet.getString("coursecode");
                courseCodes.add(courseCode);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseCodes;
        
    }
   
    public static int getClassSeats(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int seats = 0;
        
        try
        {
            getClassSeats = connection.prepareStatement("select seats from app.class where courseCode = ? and semester = ?");
            getClassSeats.setString(1, courseCode);
            getClassSeats.setString(2, semester);
            resultSet = getClassSeats.executeQuery();
            
            if (resultSet.next()) {
                seats = resultSet.getInt("seats");
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return seats;
    }
 
    public static void dropClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        
        try
        {
            dropClass = connection.prepareStatement("delete from app.class where courseCode = ? and semester = ?");
            dropClass.setString(1, courseCode);
            dropClass.setString(2, semester);
            dropClass.executeUpdate();
            
            dropClassSchedule = connection.prepareStatement("delete from app.schedule where courseCode = ? and semester = ?");
            dropClassSchedule.setString(1, courseCode);
            dropClassSchedule.setString(2, semester);
            dropClassSchedule.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
