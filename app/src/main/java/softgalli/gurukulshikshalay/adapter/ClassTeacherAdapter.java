package softgalli.gurukulshikshalay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import softgalli.gurukulshikshalay.R;
import softgalli.gurukulshikshalay.model.TeacherListDataModel;

public class ClassTeacherAdapter extends BaseAdapter {

    private Activity activity;
    ArrayList<TeacherListDataModel> teacherList;

    private static LayoutInflater inflater = null;

    public ClassTeacherAdapter(Activity mActivity, ArrayList teacherList) {
        this.activity = mActivity;
        this.teacherList = teacherList;
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if (teacherList.size() <= 0)
            return 1;
        return teacherList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder {
        public TextView teacherName;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.teacherlist_spinner_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.teacherName = view.findViewById(R.id.teacherName);

            /************  Set holder with LayoutInflater ************/
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (teacherList != null && teacherList.size() > 0) {

            /************  Set Model values in Holder elements ***********/
            holder.teacherName.setText(teacherList.get(position).getName());

        }
        return view;
    }
}