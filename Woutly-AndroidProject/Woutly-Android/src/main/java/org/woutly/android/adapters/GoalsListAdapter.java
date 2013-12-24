/**
 *  Copyright 2013 Woutly Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.woutly.android.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.woutly.android.MainActivity;
import org.woutly.android.R;
import org.woutly.android.db.entities.Goal;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Adapter to display the Goals list in the Goals Fragment
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsListAdapter extends BaseAdapter{

    Context context;
    Map<Integer, Goal> mIdsMap = new TreeMap<Integer, Goal>();

    public GoalsListAdapter(Context context, Collection<Goal> goals) {
        super();
        this.context = context;
        buildIdsMap(goals);
    }

    private void buildIdsMap(Collection<Goal> goals) {
        int x = 0;
        for(Goal g : goals){
            mIdsMap.put(x++ , g);
        }
    }

    public void addItem(Goal goal){
        mIdsMap.put(mIdsMap.size(), goal);
    }

    @Override
    public int getCount() {
        return mIdsMap.size();
    }

    @Override
    public Goal getItem(int i) {
        return (mIdsMap.containsKey(i)) ? mIdsMap.get(i) : null;
    }

    @Override
    public long getItemId(int position) {
        //Goal g = getItem(position);
        return position                ;//(g != null) ? g.getId() : 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = buildView(position, convertView);
        return convertView;
    }

    /**
     * Creates the view to be displayed in the List of goals based on the
     * position of the item and a pre-existent view if available.
     * @param position The int position of the item in the goals collection
     * @param convertView The pre-existent view to transform if available
     * @return View with the correct view.
     */
    private View buildView(int position, View convertView) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_goals_list_item, null);
            holder = new ViewHolder();
            holder.goal = (TextView) convertView.findViewById(R.id.fragment_goals_list_item_txt_goal);
            holder.progress = (TextView) convertView.findViewById(R.id.fragment_goals_list_item_txt_progress);
            holder.position = position;
            convertView.setTag(holder);
        }
        Goal goal = (Goal) getItem(position);
        holder = (ViewHolder) convertView.getTag();
        convertView.getBackground().setAlpha(75);
        holder.goal.setText(goal.getGoal());
        holder.progress.setText(context.getString(R.string.fragment_goals_list_item_txt_progress));
        holder.progress.append("75%");

        return convertView;
    }

    //View Holder pattern implementation
    static class ViewHolder{
        TextView goal;
        TextView progress;
        int position;
    }
}
