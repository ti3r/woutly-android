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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import org.woutly.android.adapters.GoalsListAdapter;
import org.woutly.android.adapters.GoalsListItemSelectedListener;
import org.woutly.android.db.entities.Goal;

import java.sql.SQLException;
import java.util.Collection;

import static org.woutly.android.MainActivity.APP_TAG;
/**
 * Async Loader for loading goals collection from db
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsAsyncLoader extends AsyncTask<Void,Void, SwingRightInAnimationAdapter> {

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
    protected SwingRightInAnimationAdapter doInBackground(Void... voids) {
        Collection<Goal> goals = null;
        SwingRightInAnimationAdapter adapter = null;
        try {
            Log.d(APP_TAG,"Loading goals using back task. Thread name"+Thread.currentThread().getName());
            goals = loadGoals();

            GoalsListAdapter tmpAdapter =  new GoalsListAdapter(context, goals);
            SwingRightInAnimationAdapter tmpAdapter2 = new SwingRightInAnimationAdapter(tmpAdapter);
            SwipeDismissAdapter tmpAdapter3 = new SwipeDismissAdapter(tmpAdapter, new OnDismissCallback() {
                @Override
                public void onDismiss(AbsListView absListView, int[] ints) {
                    Toast.makeText(context, "Dismissed", Toast.LENGTH_LONG).show();
                }
            });
            adapter = tmpAdapter2;
            Log.d(APP_TAG, "Adapter created: "+adapter.toString());
        } catch (SQLException e) {
            Log.e(APP_TAG,"Error while loading the goals",e);
        } catch (Exception e){
            Log.e(APP_TAG, "Exception caught while building adapters",e);
            throw new IllegalStateException("Error while building list adapters",e);
        }

        return adapter;
    }

    @Override
    protected void onPostExecute(SwingRightInAnimationAdapter stringArrayAdapter) {
        Log.d(APP_TAG, "Goals loaded " + stringArrayAdapter.getCount() + ". Current thread name " + Thread.currentThread().getName());
        if (finalView == null){
            throw new IllegalStateException("Illegal state List View is null");
        }
        stringArrayAdapter.setAbsListView(finalView);
        finalView.setAdapter(stringArrayAdapter);

    }

    private Collection<Goal> loadGoals() throws SQLException {
        PreparedQuery statement = goalsDao.queryBuilder().limit(10l).prepare();
        return goalsDao.query(statement);
    }
}
