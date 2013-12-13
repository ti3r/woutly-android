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

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import org.woutly.android.db.WoutlyDbHelper;
import org.woutly.android.db.entities.Goal;

import java.sql.SQLException;
import java.util.Collection;

import static org.woutly.android.MainActivity.APP_TAG;
/**
 * Async Loader for loading goals collection from db
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class GoalsAsyncLoader extends AsyncTask<Void,Void,Collection<Goal>> {

    Dao goalsDao;

    public GoalsAsyncLoader(OrmLiteSqliteOpenHelper helper) {
        try {
            this.goalsDao = helper.getDao(Goal.class);
        } catch (SQLException e) {
            Log.e(APP_TAG,"Error retrieving dao for goals async loader. Failing app",e);
            throw new ExceptionInInitializerError("Error initializing Goals Async Loader. " + e.getMessage());
        }
    }

    @Override
    protected Collection<Goal> doInBackground(Void... voids) {
        Collection<Goal> goals = null;
        try {
            Log.d(APP_TAG,"Loading goals using back task. Thread name"+Thread.currentThread().getName());
            goals = loadGoals();
        } catch (SQLException e) {
            Log.e(APP_TAG,"Error while loading the goals",e);
        }

        return goals;
    }

    @Override
    protected void onPostExecute(Collection<Goal> goals) {
        Log.d(APP_TAG, "Goals loaded "+goals.size()+". Current thread name "+Thread.currentThread().getName());
    }

    private Collection<Goal> loadGoals() throws SQLException {
        PreparedQuery statement = goalsDao.queryBuilder().limit(10l).prepare();
        return goalsDao.query(statement);
    }
}
