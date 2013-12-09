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
package org.woutly.android.db.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entity Representation of the Goals table
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
@DatabaseTable(tableName = "GOALS")
public class Goal {

    @DatabaseField(columnName = "_id",unique = true, id = true)
    Integer id;
    @DatabaseField(columnName = "goal")
    String goal;
    @DatabaseField(columnName = "rank")
    Integer rank;

    public Goal(){

    }

    public Goal(String goal) {
        this.goal = goal;
        this.rank = 5;
    }

    public Goal(String goal, Integer rank) {
        this.goal = goal;
        this.rank = rank;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
