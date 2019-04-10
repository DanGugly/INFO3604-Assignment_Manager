package info3604.assignment_organizer.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.CourseController;
import info3604.assignment_organizer.models.Course;
import info3604.assignment_organizer.views.update_course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private HashMap<CheckBox, String> checkBoxMap = new HashMap<>();
    private CheckBoxGroup<String> checkBoxGroup;
    private List<Course> mCourseList;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView courseCode;
        public TextView courseName;
        public TextView courseCredits;
        public TextView courseLevel;
        public CheckBox crsCheckbox;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            courseCode = (TextView)v.findViewById(R.id.courseCode);
            courseName = (TextView)v.findViewById(R.id.courseName);
            courseCredits = (TextView)v.findViewById(R.id.courseCredits);
            courseLevel = (TextView)v.findViewById(R.id.courseLevel);

            crsCheckbox = (CheckBox) v.findViewById(R.id.crsCheckbox);
        }
    }

    public HashMap<CheckBox, String> getCheckBoxMap() {
        return checkBoxMap;
    }

    public CheckBoxGroup<String> getCheckBoxGroup() {
        return checkBoxGroup;
    }

    public void add(int position, Course course) {
        mCourseList.add(position, course);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mCourseList.size());
    }

    public void remove(int position) {
        mCourseList.remove(position);
        mRecyclerV.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCourseList.size());
    }

    public CourseAdapter(List<Course> mCourseList, Context mContext, RecyclerView mRecyclerV) {
        this.mCourseList = mCourseList;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.course_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Course course = mCourseList.get(position);

        checkBoxMap.put(holder.crsCheckbox,course.getCode());
        checkBoxGroup = new CheckBoxGroup<>(checkBoxMap,
                new CheckBoxGroup.CheckedChangeListener<String>() {
                    @Override
                    public void onCheckedChange(ArrayList<String> values) {
                        Log.d("VALUES",values.toString());
                    }
                });
        ArrayList<String> selectedValues = checkBoxGroup.getValues();

        Log.d("Checkbox list",checkBoxMap.values().toString());
        Log.d("Selected Checkboxes",selectedValues.toString());

        holder.courseCode.setText("Code: " + course.getCode());
        holder.courseName.setText("Name: " + course.getName());
        holder.courseLevel.setText("Level: " + course.getLevel());
        holder.courseCredits.setText("Credits: " + course.getCredits());
        holder.crsCheckbox.setChecked(false);

        //If I want to add an image
        //Picasso.with(mContext).load(course.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.courseImageImgV);

        //listen to single view layout click
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete course?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(course.getCode());
                        notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder cancelDialog = new AlertDialog.Builder(mContext);
                        cancelDialog.setTitle("Choose option");
                        cancelDialog.setMessage("Are you sure you want to delete this course?");

                        cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CourseController dbHelper = new CourseController(mContext);
                                dbHelper.deleteCourse(course.getCode());

                                mCourseList.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mCourseList.size());
                                notifyDataSetChanged();
                            }
                        });
                        cancelDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        cancelDialog.create().show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

    }

    private void goToUpdateActivity(String course_id){
        Intent goToUpdate = new Intent(mContext, update_course.class);
        goToUpdate.putExtra("COURSE_ID", course_id);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() { return mCourseList.size(); }
}
