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
import android.support.v7.appcompat.R;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import org.woutly.android.adapters.GoalsListAdapter;
import org.woutly.android.adapters.GoalsListItemSelectedListener;
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
public class GoalsAsyncLoader extends AsyncTask<Void,Void, SwingRightInAnimationAdapter> {

    Dao goalsDao;
    Context context;
    ListView finalView;
    GoalsListItemSelectedListener listener;

    public GoalsAsyncLoader(Context context, OrmLiteSqliteOpenHelper helper, ListView listView,
                            GoalsListItemSelectedListener listener) {
        try {
            this.goalsDao = helper.getDao(Goal.class);
            this.context = context;
            this.finalView = listView;
            this.listener = listener;
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

//        finalView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (listener == null)
//                    throw new IllegalStateException("Item selected from Goals list but listener null");
//                listener.onItemSelectedByLongClick((Goal) parent.getItemAtPosition(position), (GoalsListAdapter) parent.getAdapter(), view);
//                return false;
//            }
//        });
    }

    private Collection<Goal> loadGoals() throws SQLException {
        PreparedQuery statement = goalsDao.queryBuilder().limit(10l).prepare();
        return goalsDao.query(statement);
    }
}
