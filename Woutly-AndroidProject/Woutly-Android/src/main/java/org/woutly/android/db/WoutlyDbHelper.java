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
package org.woutly.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * DBHelper for ORMLite in the App
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
public class WoutlyDbHelper extends OrmLiteSqliteOpenHelper{

    private static final int DB_HEAD_VERSION = 1;
    private static final String DB_NAME = "org.woutly.android.Database";

    public WoutlyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_HEAD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            DatabaseConnection connection = connectionSource.getReadWriteConnection();
            Savepoint savePoint = null;

            //if (connection.isAutoCommitSupported()){
            //    connection.setAutoCommit(true);
            //}else{
            //    savePoint = connection.setSavePoint("createSavePoint");
            //}

            connection.executeStatement("CREATE TABLE GOALS (_id INTEGER PRIMARY KEY AUTOINCREMENT, goal TEXT, rank INTEGER default 5)",
                    DatabaseConnection.DEFAULT_RESULT_FLAGS);

            //Commit if not commit supported
            //if (!connection.isAutoCommitSupported())
            //    connection.commit(savePoint);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to create Database for Application.",e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {

    }
}
