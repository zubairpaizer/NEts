package com.fyp.faaiz.ets.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.fyp.faaiz.ets.R;
import com.shawnlin.numberpicker.NumberPicker;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  View rootView;
    private Button done;
    private Button cancel;
    private NumberPicker hour;
    private NumberPicker minute;
    private NumberPicker second;
    private Firebase firebase;


    private OnFragmentInteractionListener mListener;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        firebase = new Firebase("https://nets-8cb47.firebaseio.com/employees/" + mParam2);

        hour = (NumberPicker) rootView.findViewById(R.id.number_picker_hour);
        minute = (NumberPicker) rootView.findViewById(R.id.number_picker_minute);
        second = (NumberPicker) rootView.findViewById(R.id.number_picker_second);
        done = (Button) rootView.findViewById(R.id.timer_done);
        cancel = (Button) rootView.findViewById(R.id.timer_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(TimerFragment.this).commit();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase tracking_time = firebase.child("tracking_interval");
                int selected_hour = hour.getValue();
                int selected_minute = minute.getValue();
                int selected_second = second.getValue();

                String source = "0" + selected_hour + ":" +  selected_minute + ":" + selected_second;
                Toast.makeText(getActivity(), source, Toast.LENGTH_SHORT).show();
                String[] tokens = source.split(":");
                int secondsToMs = Integer.parseInt(tokens[2]) * 1000;
                int minutesToMs = Integer.parseInt(tokens[1]) * 60000;
                int hoursToMs = Integer.parseInt(tokens[0]) * 3600000;
                long total = secondsToMs + minutesToMs + hoursToMs;
                Toast.makeText(getActivity(), total + "", Toast.LENGTH_SHORT).show();
                tracking_time.setValue(total);
                Toast.makeText(getActivity(), mParam2, Toast.LENGTH_SHORT).show();

            }//
        });

        hour.setDividerColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));

        // set selected text color
        hour.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        hour.setSelectedTextColorResource(R.color.colorPrimary);

        // set text color
        hour.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        hour.setTextColorResource(R.color.grey);

        minute.setDividerColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        // set selected text color
        minute.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        minute.setSelectedTextColorResource(R.color.colorPrimary);

        // set text color
        minute.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        minute.setTextColorResource(R.color.grey);


        second.setDividerColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
        // set selected text color
        second.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        second.setSelectedTextColorResource(R.color.colorPrimary);

        // set text color
        second.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        second.setTextColorResource(R.color.grey);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                    //+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
