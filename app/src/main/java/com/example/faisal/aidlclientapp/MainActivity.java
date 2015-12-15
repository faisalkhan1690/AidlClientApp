package com.example.faisal.aidlclientapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.faisal.aidlserverapp.IAdd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etValue1;
    private EditText etValue2;
    private TextView mSum;
    private Button bAdd;
    protected IAdd AddService;
    ServiceConnection AddServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etValue1 = (EditText) findViewById(R.id.value1);
        etValue2 = (EditText) findViewById(R.id.value2);
        mSum = (TextView) findViewById(R.id.sum);
        bAdd = (Button) findViewById(R.id.add);
        bAdd.setOnClickListener(this);
        initConnection();
    }

    void initConnection() {
        AddServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                AddService = null;
                Toast.makeText(getApplicationContext(), "Service Disconnected",
                        Toast.LENGTH_SHORT).show();
                Log.d("IRemote", "Binding - Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                AddService = IAdd.Stub.asInterface((IBinder) service);
                Toast.makeText(getApplicationContext(),
                        "Addition Service Connected", Toast.LENGTH_SHORT)
                        .show();
                Log.d("IRemote", "Binding is done - Service connected");
            }
        };
        if (AddService == null) {
            Intent it = new Intent();
            it.setAction("service.Calculator");
            // binding to remote service
            bindService(it, AddServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(AddServiceConnection);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add: {
                int num1 = Integer.parseInt(etValue1.getText().toString());
                int num2 = Integer.parseInt(etValue2.getText().toString());
                try {
                    mSum.setText("Result:" + AddService.add(num1, num2));
                    Log.d("IRemote", "Binding - Add operation");
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}

