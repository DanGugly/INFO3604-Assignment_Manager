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

import com.google.android.material.card.MaterialCardView;
import com.xeoh.android.checkboxgroup.CheckBoxGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import info3604.assignment_organizer.R;
import info3604.assignment_organizer.controllers.AssignmentController;
import info3604.assignment_organizer.models.Assignment;
import info3604.assignment_organizer.views.update_assignment;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    
    private List<Assignment> mAssignmentList;
    private HashMap<CheckBox, String> checkBoxMap = new HashMap<>();
    private CheckBoxGroup<String> checkBoxGroup;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView courseCode;
        public TextView assignmentTitle;
        public TextView dueDate;
        public CheckBox asgCheckbox;
        public MaterialCardView materialCardView;
        public TextView assProgress;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            courseCode = (TextView)v.findViewById(R.id.courseCode);
            assignmentTitle = (TextView)v.findViewById(R.id.assignmentTitle);
            dueDate = (TextView)v.findViewById(R.id.dueDate);
            asgCheckbox = (CheckBox) v.findViewById(R.id.asgCheckbox);
            materialCardView = (MaterialCardView)v.findViewById(R.id.assignment_cardView);
            assProgress = (TextView)v.findViewById(R.id.assProgress);
        }
    }

    public CheckBoxGroup<String> getCheckBoxGroup() {
        return checkBoxGroup;
    }

    public void add(int position, Assignment assignment) {
        mAssignmentList.add(position, assignment);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mAssignmentList.size());
    }

    public void remove(int position) {
        mAssignmentList.remove(position);
        mRecyclerV.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAssignmentList.size());
    }

    public AssignmentAdapter(List<Assignment> mAssignmentList, Context mContext, RecyclerView mRecyclerV) {
        this.mAssignmentList = mAssignmentList;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    @NonNull
    @Override
    public AssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.assignment_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        AssignmentAdapter.ViewHolder vh = new AssignmentAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Assignment assignment = mAssignmentList.get(position);

        checkBoxMap.put(holder.asgCheckbox,String.valueOf(assignment.getAssignmentID()));
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

        holder.courseCode.setText("Course Code: " + assignment.getCourseID());
        holder.assignmentTitle.setText("Assignment Title: " + assignment.getTitle());
        holder.dueDate.setText("Due Date: " + assignment.getDueDate());
        String progressText = "Progress: ";
        holder.asgCheckbox.setChecked(false);

        Log.d("Progress",assignment.getTitle()+ " " +assignment.getProgress());

        if(assignment.getProgress()==0){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Ongoing));
            progressText += "Ongoing";
        }
        else if(assignment.getProgress()==1){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Completed));
            progressText += "Completed";
        }
        else if(assignment.getProgress()==-1){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Missed));
            progressText += "Overdue";
        }

        holder.assProgress.setText(progressText);

        //If I want to add an image
        //Picasso.with(mContext).load(assignment.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.assignmentImageImgV);

        //listen to single view layout click
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete assignment?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(assignment.getAssignmentID());
                        notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder cancelDialog = new AlertDialog.Builder(mContext);
                        cancelDialog.setTitle("Choose option");
                        cancelDialog.setMessage("Are you sure you want to delete this assignment?");

                        cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AssignmentController dbHelper = new AssignmentController(mContext);
                                dbHelper.deleteAssignment(assignment.getAssignmentID());

                                mAssignmentList.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mAssignmentList.size());
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

    private void goToUpdateActivity(int assignment_id){
        Intent goToUpdate = new Intent(mContext, update_assignment.class);
        goToUpdate.putExtra("ASSIGNMENT_ID", assignment_id);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() { return mAssignmentList.size(); }
}
