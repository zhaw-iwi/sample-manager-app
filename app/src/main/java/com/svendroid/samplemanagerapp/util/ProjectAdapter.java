package com.svendroid.samplemanagerapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.svendroid.samplemanagerapp.R;
import com.svendroid.samplemanagerapp.data.UserDbHelper;
import com.svendroid.samplemanagerapp.model.ProjectBasic;
import com.svendroid.samplemanagerapp.model.User;

/**
 * Created by svhe on 25.05.2016.
 */

public class ProjectAdapter extends ArrayAdapter<ProjectBasic> {

    public static final int CHECK_IN = 0;
    public static final int CHECK_OUT = 1;

    private ProjectBasic[] values;
    private LayoutInflater inflater;
    private User user;

    public ProjectAdapter(Context context, ProjectBasic[] values) {
        super(context,  -1, values);
        this.values = values;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        UserDbHelper userDbHelper = new UserDbHelper(context);
        User user = userDbHelper.getAll().get(0);
        userDbHelper.close();
        this.user = user;
    }

    public void setItems(ProjectBasic[] values) {
        this.values = values;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;


        if ( convertView == null )
        {
            convertView = inflater.inflate(R.layout.project_row, null);
            holder = new ViewHolder();
            holder.projectName = (TextView) convertView.findViewById( R.id.projectLabel );
            holder.projectImage = (ImageView) convertView.findViewById( R.id.projectImage );
            holder.projectCheck = (ImageView) convertView.findViewById(R.id.projectCheck);
            holder.projectFrameLayout = (FrameLayout) convertView.findViewById(R.id.projectFrameLayout);


            convertView.setTag (holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag ();
        }

        holder.projectName.setText(values[position].getName());

        setChecked(values[position].isCheckedIn(), holder.projectCheck);

        try {
            String colorString = values[position].getImageUrl();
            colorString = "#" + colorString.substring(30, 36);
            holder.projectImage.setBackgroundColor(Color.parseColor(colorString));
        } catch (Exception e) {

        }
        final ProjectBasic project = values[position];
        holder.projectFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                project.setCheckedIn(!project.isCheckedIn());
                project.setDirty(true);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private void setChecked(final boolean checked, final ImageView projectCheck) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int longAnimTime = getContext().getResources().getInteger(android.R.integer.config_longAnimTime);

            projectCheck.setVisibility(!checked ? View.GONE : View.VISIBLE);
            projectCheck.animate().setDuration(longAnimTime).alpha(
                    !checked ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    projectCheck.setVisibility(!checked ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            projectCheck.setVisibility(!checked ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
        //return values[position].isCheckedIn() ? 1 : 0;
    }

    static class ViewHolder{
        TextView projectName;
        ImageView projectImage;
        ImageView projectCheck;
        FrameLayout projectFrameLayout;
    }
}

