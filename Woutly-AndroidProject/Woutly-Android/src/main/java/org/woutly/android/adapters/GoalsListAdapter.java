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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.woutly.android.R;
import org.woutly.android.db.entities.Goal;

import java.util.Collection;

/**
 * Adapter to display the Goals list in the Goals Fragment
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsListAdapter extends BaseAdapter{

    Collection<Goal> goals;
    Context context;

    public GoalsListAdapter(Context context, Collection<Goal> goals) {
        this.context = context;
        this.goals = goals;
    }

    @Override
    public int getCount() {
        return (goals != null)? goals.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (goals != null) ? goals.toArray()[position] : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_goals_list_item, null);
        Goal goal = (Goal) getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.fragment_goals_list_item_txt_goal);
        text.setText(goal.getGoal());
        ProgressBar prg = (ProgressBar) convertView.findViewById(R.id.fragment_goals_list_item_prg_progress);
        prg.setProgress(75);
        prg.setMax(100);
        return convertView;
    }
}
