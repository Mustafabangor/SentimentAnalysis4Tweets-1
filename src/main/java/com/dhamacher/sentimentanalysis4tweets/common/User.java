/* Copyright 2013 Daniel Hamacher, Mustafa Elkhunni
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dhamacher.sentimentanalysis4tweets.common;

/**
 *
 * @author daniel, mustafa
 */
public class User {
    long id;
    int followersCount;
    int friendsCount;
    int statusesCount;

    public User(long id, int followersCount, int friendsCount, int statusesCount) {
        this.id = id;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.statusesCount = statusesCount;
    }
    
    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public long getId() {
        return id;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", followersCount=" + followersCount + ", friendsCount=" + friendsCount + ", statusesCount=" + statusesCount + '}';
    }
    
}
