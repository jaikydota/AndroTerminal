package com.jaiky.shell.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity extends ShellActivity {

    private Button btnRun;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnRun = (Button) findViewById(R.id.btnRun);
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCmd();
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_test;
    }


//    private DataOutputStream dos;
//    private DataInputStream dis;
//    private Process process;

    //命令阻塞
//    private String exec(String command) {
//        try {
//            if (process == null)
//                process = Runtime.getRuntime().exec("su");
//            if (dos == null)
//                dos = new DataOutputStream(process.getOutputStream());
//            if (dis == null)
//                dis = new DataInputStream(process.getInputStream());
//
//            dos.writeBytes(command + "\n");
//            dos.flush();
////            dos.close();
//
//
//            byte[] b = new byte[4096];
//            StringBuffer output = new StringBuffer();
//            dis.read(b, 0 , b.length);
////            int read;
////            while ((read = dis.read(b, 0 , b.length)) > 0) {
////                output.append(new String(b, 0, read));
//                output.append(new String(b, 0, b.length));
////            }
//
////            dis.close();
//
////            process.waitFor();
//            return output.toString();
//        } catch (IOException e) {
////            throw new RuntimeException(e);
//            return "错误的命令\n";
//        }
////        catch (InterruptedException e) {
//////            throw new RuntimeException(e);
////            return "shell已中断\n";
////        }
//    }

    //单命令执行
//    private String exec(String command) {
//        try {
//            Process process = Runtime.getRuntime().exec(command);
//            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
//            outputStream.writeBytes(command + "\n");
//            outputStream.flush();
//            outputStream.close();
//
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            int read;
//            char[] buffer = new char[4096];
//            StringBuffer output = new StringBuffer();
//            while ((read = reader.read(buffer)) > 0) {
//                output.append(buffer, 0, read);
//            }
//            reader.close();
//
//            process.waitFor();
//            return output.toString();
//        } catch (IOException e) {
////            throw new RuntimeException(e);
//            return "错误的命令\n";
//        }
//        catch (InterruptedException e) {
////            throw new RuntimeException(e);
//            return "shell已中断\n";
//        }
//    }




}