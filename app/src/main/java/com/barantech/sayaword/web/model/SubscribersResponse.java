package com.barantech.sayaword.web.model;

import java.util.ArrayList;

/**
 * Created by mary on 11/23/15.
 */
public class SubscribersResponse {
    public ArrayList<Subscriber> subscribers;

    public static class Subscriber{
        public int id;
        public int word_id;
        public String user_id;
        public int repeat;
        public String modified_date;

    }
}
