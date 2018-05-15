package softgalli.gurukulshikshalay.calender;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import softgalli.gurukulshikshalay.model.StudentListDataModel;


public class RealMController {

    private static RealMController instance;
    private final Realm realm;

    public RealMController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealMController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealMController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealMController with(Activity activity) {

        if (instance == null) {
            instance = new RealMController(activity.getApplication());
        }
        return instance;
    }

    public static RealMController with(Application application) {

        if (instance == null) {
            instance = new RealMController(application);
        }
        return instance;
    }

    public static RealMController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from StudentListDataModel.class
    public void clearAll() {
        realm.beginTransaction();
        realm.commitTransaction();
    }

    //find all objects in the StudentListDataModel.class
    public RealmResults<StudentListDataModel> getStudentsList() {
        return realm.where(StudentListDataModel.class).findAll();
    }

    //query a single item with the given id
    public StudentListDataModel getStudent(String id) {

        return realm.where(StudentListDataModel.class).equalTo("id", id).findFirst();
    }

    //check if StudentListDataModel.class is empty
    public boolean hasStudents() {
        return !realm.where(StudentListDataModel.class).findAll().isEmpty();
    }

    //query example
    public RealmResults<StudentListDataModel> queryedStudents() {

        return realm.where(StudentListDataModel.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}