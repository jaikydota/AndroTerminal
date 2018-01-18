package com.jaiky.shell.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaiky.shell.utils.AndroidUtils;
import com.jaiky.shell.utils.ShellUtils;

import eu.darken.rxshell.cmd.Cmd;
import eu.darken.rxshell.cmd.RxCmdShell;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ShellActivity extends Activity implements View.OnClickListener {

    private Button btnOpenRemoteAdb, btnReboot;
    private EditText etCmd;
    private TextView tvOut;
    private ScrollView scOut;

    private RxCmdShell.Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        tvOut = (TextView) findViewById(R.id.tvOut);
        scOut = (ScrollView) findViewById(R.id.scOut);
        etCmd = (EditText) findViewById(R.id.etCmd);
        btnOpenRemoteAdb = (Button) findViewById(R.id.btnOpenRemoteAdb);
        btnReboot = (Button) findViewById(R.id.btnReboot);
        btnOpenRemoteAdb.setOnClickListener(this);
        btnReboot.setOnClickListener(this);

        etCmd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    runCmd();
                    return true;
                }
                else {
                    return false;
                }
            }
        });
    }

    public int getContentView(){
        return R.layout.activity_shell;
    }


    @Override
    protected void onResume() {
        super.onResume();
        RxCmdShell rxCommandShell = RxCmdShell.builder().root(true).build();
//        RxCmdShell rxCommandShell = RxCmdShell.builder().build();

        rxCommandShell.open()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(session -> {
                    ShellActivity.this.session = session;
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        session.close()
                .doOnSubscribe(d -> session = null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                });
    }


    public void runCmd() {
        String text = etCmd.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            String command = text;
            Cmd.builder(command).submit(session)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        etCmd.setText("");
                        if (result.merge().size() == 0)
                            tvOut.append(command + " execution succeed\n");
                        for (String o : result.merge())
                            tvOut.append(o + "\n");
                        appendSplit();
                        scrollToBottom(scOut, tvOut);
                    });
        }
    }


    private void appendSplit(){
        tvOut.append("-----------------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    @Override
    public void onClick(View v) {
        if (v == btnOpenRemoteAdb) {
            if (ShellUtils.openRemoteAdb()) {
                String ipAddress = AndroidUtils.getLocalIp();
                if (TextUtils.isEmpty(ipAddress.trim()))
                    ipAddress = AndroidUtils.getIPAddress(ShellActivity.this);
                String info = String.format("请在PC终端adb输入:\nadb connect %s\n", ipAddress);
                Toast.makeText(ShellActivity.this, info, Toast.LENGTH_SHORT).show();
                tvOut.append(info);
                appendSplit();
            }
            else {
                Toast.makeText(ShellActivity.this, "错误，该手机未ROOT", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == btnReboot) {
            if (!ShellUtils.rebootSystem())
                Toast.makeText(ShellActivity.this, "错误，该手机未ROOT", Toast.LENGTH_SHORT).show();
        }
    }



    private Handler handler = new Handler();
    private void scrollToBottom(final ScrollView scrollView, final View view) {
        handler.post(() -> {
            if (scrollView == null || view == null) {
                return;
            }
            int offset = view.getMeasuredHeight() - scrollView.getMeasuredHeight();
            if (offset < 0) {
                offset = 0;
            }
            scrollView.scrollTo(0, offset);
        });
    }


}