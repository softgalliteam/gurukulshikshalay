package softgalli.gurukulshikshalay.calender;


import android.text.TextUtils;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import softgalli.gurukulshikshalay.model.StudentListDataModel;

/**
 * @author ashraf
 */
public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER = "StudentListDataModel Id,StudentListDataModel Name,Father's Name,Gender,Age,Attendance";
    private static final String TAG = CsvFileWriter.class.getSimpleName();

    public static void writeCsvFile(String fileName, List<StudentListDataModel> studentsList) {

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (int i = 0; i < studentsList.size(); i++) {
                if (i < studentsList.size()) {
                    StudentListDataModel studentListDataModel = studentsList.get(i);
                    if (studentListDataModel != null) {
                        if (!TextUtils.isEmpty(String.valueOf(studentListDataModel.getStudentId())))
                            fileWriter.append(String.valueOf(studentListDataModel.getStudentId()));
                        fileWriter.append(COMMA_DELIMITER);
                        if (studentListDataModel.getStudentName() != null && !TextUtils.isEmpty(studentListDataModel.getStudentName()))
                            fileWriter.append(studentListDataModel.getStudentName());
                        fileWriter.append(COMMA_DELIMITER);
                        if (studentListDataModel.getFatherName() != null && !TextUtils.isEmpty(studentListDataModel.getFatherName()))
                            fileWriter.append(studentListDataModel.getFatherName());
                        fileWriter.append(COMMA_DELIMITER);
                        if (studentListDataModel.getGender() != null && !TextUtils.isEmpty(studentListDataModel.getGender()))
                            fileWriter.append(studentListDataModel.getGender());
                        fileWriter.append(COMMA_DELIMITER);
                        if (!TextUtils.isEmpty(String.valueOf(studentListDataModel.getAge())))
                            fileWriter.append(String.valueOf(studentListDataModel.getAge()));
                        fileWriter.append(COMMA_DELIMITER);
                        if (!TextUtils.isEmpty(String.valueOf(studentListDataModel.isSelected())))
                            fileWriter.append(String.valueOf(studentListDataModel.isSelected()));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                    }
                }
            }

            Log.i(TAG, "CSV file was created successfully !!!");

        } catch (Exception e) {
            Log.e(TAG, "Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}