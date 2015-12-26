package com.barantech.sayaword;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.barantech.sayaword.util.Log;
import com.barantech.sayaword.web.Constant;
import com.barantech.sayaword.web.ErrorListener;
import com.barantech.sayaword.web.StringRequestWord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    TextView mTvResult;
    private EditText mEtWord;
    private EditText mEtID;
    Context mContext;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = getContext();
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mEtWord = (EditText) view.findViewById(R.id.et_word);
        mEtID = (EditText) view.findViewById(R.id.et_id);
        final View mBtnSend = view.findViewById(R.id.btn_sendword);

        mEtWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    mBtnSend.setVisibility(View.VISIBLE);
                else
                    mBtnSend.setVisibility(View.INVISIBLE);
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWord();
            }
        });
        return view;
    }

    private void sendWord() {
        mTvResult.setVisibility(View.INVISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        HashMap<String, String> param = new HashMap<>(2);
        final String word = mEtWord.getText().toString();
        param.put("word", word);
        param.put("userId", mEtID.getText().toString());

        // Request a string response from the provided URL.
        StringRequestWord stringRequestWord = new StringRequestWord(Request.Method.POST,
                Constant.WORDS, param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("owner")) {
                        mTvResult.setVisibility(View.VISIBLE);
                        String[] args = new String[1];
                        args[0] = word;
                        mTvResult.setText(getString(R.string.word_owner_message, args));
                    } else {
                        if (jsonObject.has("subscribers")) {
                            Intent intent = new Intent(getActivity(), SubscribersActivity.class);
                            intent.putExtra(SubscribersActivity.EXTRA_SUBSCRIBERS, response);
                            startActivity(intent);
                        }
                        mTvResult.setVisibility(View.VISIBLE);
                        String[] args = new String[1];
                        args[0] = "5";
                        mTvResult.setText(getString(R.string.word_subscribers, args));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener(new ErrorListener.ErrorHandler() {
            @Override
            public void onErrorResponse(String message) {
                Log.e("error", message);
                mEtWord.setError(message);
            }
        }));

        // Add the request to the RequestQueue.
        queue.add(stringRequestWord);
    }
}
