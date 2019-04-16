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
import info3604.assignment_organizer.controllers.CheckpointController;
import info3604.assignment_organizer.controllers.MainController;
import info3604.assignment_organizer.models.Checkpoint;
import info3604.assignment_organizer.views.update_checkpoint;


public class CheckpointAdapter extends RecyclerView.Adapter<CheckpointAdapter.ViewHolder>  {

    private HashMap<CheckBox, String> checkBoxMap = new HashMap<>();
    private CheckBoxGroup<String> checkBoxGroup;
    private List<Checkpoint> mCheckpointList;
    private Context mContext;
    private RecyclerView mRecyclerV;
    private MainController MC;



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView chkCourseCode;
        public TextView assignmentTitle;
        public TextView checkpointTitle;
        public TextView dueDate;
        public TextView chkProgress;
        public CheckBox chkCheckbox;
        public MaterialCardView materialCardView;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            chkCourseCode = (TextView)v.findViewById(R.id.chkCourseCode);
            checkpointTitle = (TextView)v.findViewById(R.id.checkpointTitle);
            assignmentTitle = (TextView)v.findViewById(R.id.assignmentTitle);
            dueDate = (TextView)v.findViewById(R.id.dueDate);
            chkCheckbox = (CheckBox) v.findViewById(R.id.chkCheckbox);
            materialCardView = (MaterialCardView)v.findViewById(R.id.checkpoint_cardView);
            chkProgress = (TextView)v.findViewById(R.id.chkProgress);
        }
    }

    public CheckBoxGroup<String> getCheckBoxGroup() {
        return checkBoxGroup;
    }

    public void add(int position, Checkpoint checkpoint) {
        mCheckpointList.add(position, checkpoint);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mCheckpointList.size());
    }

    public void remove(int position) {
        mCheckpointList.remove(position);
        mRecyclerV.removeViewAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCheckpointList.size());
    }

    public CheckpointAdapter(List<Checkpoint> mCheckpointList, Context mContext, RecyclerView mRecyclerV) {
        this.mCheckpointList = mCheckpointList;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    @NonNull
    @Override
    public CheckpointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MC = new MainController(mContext);

        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.checkpoint_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        CheckpointAdapter.ViewHolder vh = new CheckpointAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckpointAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Checkpoint checkpoint = mCheckpointList.get(position);

        checkBoxMap.put(holder.chkCheckbox,String.valueOf(checkpoint.getCheckpointID()));
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

        holder.chkCourseCode.setText("Course Code: " + MC.getAssignment(checkpoint.getAssignmentID()).getCourseID());
        Log.d("COURSE CODE:", "" + MC.getAssignment(checkpoint.getAssignmentID()).getCourseID());
        holder.assignmentTitle.setText("Assignment Title: " + MC.getAssignment(checkpoint.getAssignmentID()).getTitle());
        holder.checkpointTitle.setText("Checkpoint Title: " + checkpoint.getTitle());
        holder.dueDate.setText("Due Date: " + checkpoint.getDueDate());
        holder.chkCheckbox.setChecked(false);

        String progressText = "Progress: ";
        if(checkpoint.getProgress()==0){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Ongoing));
            progressText += "Ongoing";
        }
        else if(checkpoint.getProgress()==1){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Completed));
            progressText += "Completed";
        }
        else if(checkpoint.getProgress()==-1){
            holder.materialCardView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.Missed));
            progressText += "Overdue";
        }

        holder.chkProgress.setText(progressText);

        //If I want to add an image
        //Picasso.with(mContext).load(checkpoint.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.checkpointImageImgV);

        //listen to single view layout click
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                builder.setMessage("Update or delete checkpoint?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(checkpoint.getCheckpointID());
                        notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder cancelDialog = new AlertDialog.Builder(mContext);
                        cancelDialog.setTitle("Choose option");
                        cancelDialog.setMessage("Are you sure you want to delete this checkpoint?");

                        cancelDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CheckpointController dbHelper = new CheckpointController(mContext);
                                dbHelper.deleteCheckpoint(checkpoint.getCheckpointID());

                                mCheckpointList.remove(position);
                                mRecyclerV.removeViewAt(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mCheckpointList.size());
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

    private void goToUpdateActivity(int checkpoint_id){
        Intent goToUpdate = new Intent(mContext, update_checkpoint.class);
        goToUpdate.putExtra("CHECKPOINT_ID", checkpoint_id);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() { return mCheckpointList.size(); }
}
