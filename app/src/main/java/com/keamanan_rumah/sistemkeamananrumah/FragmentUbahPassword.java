package com.keamanan_rumah.sistemkeamananrumah;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentUbahPassword extends Fragment {


    boolean loaddata;
    ProgressDialog pDialog;

    List<NameValuePair> data_password = new ArrayList<NameValuePair>(3);

    String strPassLama, strPassBaru, strPassKonfirmasi;

    LinearLayout llNotif;
    TextView tvNotif;
    EditText etPassLama, etPassBaru, etPassKonfirmasi;
    Button btnSimpan;

    String status_cek,message,message_severity;
    String JSON_data;

    public FragmentUbahPassword() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterUbahPassword = inflater.inflate(R.layout.fragment_ubah_password, container, false);
        llNotif = (LinearLayout) inflaterUbahPassword.findViewById(R.id.llNotif);
        tvNotif = (TextView) inflaterUbahPassword.findViewById(R.id.tvNotif);
        etPassLama = (EditText) inflaterUbahPassword.findViewById(R.id.etPassLama);
        etPassBaru = (EditText) inflaterUbahPassword.findViewById(R.id.etPassBaru);
        etPassKonfirmasi = (EditText) inflaterUbahPassword.findViewById(R.id.etPassKonfirmasi);
        btnSimpan = (Button) inflaterUbahPassword.findViewById(R.id.btnSimpan);
        return inflaterUbahPassword;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPassLama = etPassLama.getText().toString().trim();
                strPassBaru = etPassBaru.getText().toString().trim();
                strPassKonfirmasi = etPassKonfirmasi.getText().toString().trim();
                llNotif.setVisibility(View.VISIBLE);
                if(strPassLama.equals("") || strPassLama==null ||
                strPassBaru.equals("") || strPassBaru==null ||
                strPassKonfirmasi.equals("") || strPassKonfirmasi==null){
                    tvNotif.setText("Semua field harus diisi!");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else{
                    if(!strPassBaru.equals(strPassKonfirmasi) || strPassBaru.length() < 6 || strPassKonfirmasi.length() < 6){
                        llNotif.setVisibility(View.VISIBLE);
                        tvNotif.setText("Panjang password pengganti minimal 6 digit! Pastikan juga bahwa password baru dan password konfirmasi sesuai.");
                        tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                    }else{
                        data_password.add(new BasicNameValuePair("password",strPassLama ));
                        data_password.add(new BasicNameValuePair("new_password", strPassBaru));
                        new AsyncUpdatePassword().execute();
                    }
                }
            }
        });
    }

    private class AsyncUpdatePassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            String url = RootActivity.api_update_password;
            JSON_data = sh.makeServiceCall(url, HTTPSvc.POST, data_password);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    status_cek = response.getString("status_cek");
                    message = response.getString("message");
                    message_severity = response.getString("message_severity");
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata){
                tvNotif.setText(message);
                if(message_severity.equals("success")){
                    etPassLama.setText("");etPassBaru.setText("");etPassKonfirmasi.setText("");
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else
                if(message_severity.equals("warning")){
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else
                if(message_severity.equals("danger")){
                    tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                }
            }else{
                tvNotif.setText("Error !");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }

}