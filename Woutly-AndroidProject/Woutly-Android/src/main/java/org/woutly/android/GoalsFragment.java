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
package org.woutly.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import org.woutly.android.adapters.GoalsListAdapter;
import org.woutly.android.adapters.GoalsListItemSelectedListener;
import org.woutly.android.db.WoutlyDbHelper;
import org.woutly.android.db.entities.Goal;
import org.woutly.android.loaders.GoalsAsyncLoader;

import java.sql.SQLException;

import static org.woutly.android.MainActivity.APP_TAG;
/**
 * Fragment to display/create goals
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsFragment extends Fragment implements View.OnClickListener, GoalsListItemSelectedListener {

    EditText edtGoal = null;
    Button btnAdd = null;
    OrmLiteSqliteOpenHelper helper = null;
    ListView frmGoals;

    public static GoalsFragment newInstance(){
        GoalsFragment fragment = new GoalsFragment();
        return fragment;
    }

    protected GoalsFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
       super.onAttach(activity);
       helper = OpenHelperManager.getHelper(activity, WoutlyDbHelper.class);
    }

    private void executeloadGoals() {
            GoalsAsyncLoader loader = new GoalsAsyncLoader(getActivity(), helper, frmGoals, this);
            loader.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        OpenHelperManager.releaseHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goals, null);
        edtGoal = (EditText) v.findViewById(R.id.fragment_goals_edt_goal);
        btnAdd = (Button) v.findViewById(R.id.fragment_goals_btn_add);
        btnAdd.setOnClickListener(this);
        frmGoals = (ListView) v.findViewById(R.id.fragment_goals_frm_goals);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        executeloadGoals();
    }

    private void saveGoal() throws SQLException{
        Dao dao = helper.getDao(Goal.class);
        Goal g = new Goal(edtGoal.getText().toString());
        dao.create(g);
    }

    //Click implementations
    @Override
    public void onClick(View view) {
        if (view == btnAdd){
            try{
                saveGoal();
            }catch(SQLException e){
                Log.e(APP_TAG, "Unable to save goal", e);
            }
        }
    }

    @Override
    public void onItemSelectedByLongClick(Goal goal, GoalsListAdapter adapter,  View v) {
        Toast.makeText(getActivity(),
                String.format("Goal item with id %s selected", goal.getId()) , Toast.LENGTH_LONG).show();
        try {
            Dao dao = helper.getDao(Goal.class);
            dao.delete(goal);
            adapter.notifyDataSetChanged();
            frmGoals.invalidate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
