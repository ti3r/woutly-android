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
package org.woutly.android.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import org.woutly.android.db.entities.Goal;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.woutly.android.MainActivity.APP_TAG;
/**
 * Async Loader for loading goals collection from db
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsAsyncLoader extends AsyncTask<Void,Void,ArrayAdapter<String>> {

    Dao goalsDao;
    Context context;
    ListView finalView;

    public GoalsAsyncLoader(Context context, OrmLiteSqliteOpenHelper helper, ListView listView) {
        try {
            this.goalsDao = helper.getDao(Goal.class);
            this.context = context;
            this.finalView = listView;
        } catch (SQLException e) {
            Log.e(APP_TAG,"Error retrieving dao for goals async loader. Failing app",e);
            throw new ExceptionInInitializerError("Error initializing Goals Async Loader. " + e.getMessage());
        }
    }

    @Override
    protected ArrayAdapter<String> doInBackground(Void... voids) {
        Collection<Goal> goals = null;
        ArrayAdapter<String> adapter = null;
        List<String> titles = new LinkedList<String>();
        try {
            Log.d(APP_TAG,"Loading goals using back task. Thread name"+Thread.currentThread().getName());
            goals = loadGoals();
            for(Goal goal : goals){
                titles.add(goal.getGoal());
            }
            Object[] array =  titles.toArray();
            adapter =  new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, array);
            Log.d(APP_TAG, "Adapter created: "+adapter.toString());
        } catch (SQLException e) {
            Log.e(APP_TAG,"Error while loading the goals",e);
        }

        return adapter;
    }

    @Override
    protected void onPostExecute(ArrayAdapter<String> stringArrayAdapter) {
        Log.d(APP_TAG, "Goals loaded " + stringArrayAdapter.getCount() + ". Current thread name " + Thread.currentThread().getName());
        if (finalView == null){
            throw new IllegalStateException("Illegal state List View is null");
        }
        finalView.setAdapter(stringArrayAdapter);
        stringArrayAdapter.notifyDataSetChanged();
    }

    private Collection<Goal> loadGoals() throws SQLException {
        PreparedQuery statement = goalsDao.queryBuilder().limit(10l).prepare();
        return goalsDao.query(statement);
    }
}
