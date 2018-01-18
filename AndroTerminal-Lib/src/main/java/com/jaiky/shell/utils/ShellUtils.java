package com.jaiky.shell.utils;

import java.io.DataOutputStream;

/**  
 * Author by Jaiky, Date on 2017/12/6.
 */
public class ShellUtils {

    /**
     * 打开远程ADB功能
     * @return
     */
    public static boolean openRemoteAdb(){
        String[] commandList = new String[]{
                "setprop service.adb.tcp.port 5555",
                "stop adbd",
                "start adbd"
        };
        return ShellUtils.exeCmdByRoot(commandList);
    }


    /**
     * 重启APP
     * @return
     */
    public static boolean rebootApp(String packName, String activityPack){
        String[] commandList = new String[]{
                "am force-stop " + packName,
                "am start -n " + packName + "/" + activityPack
        };
        return ShellUtils.exeCmdByRoot(commandList);
    }


    /**
     * 重启系统
     * @return
     */
    public static boolean rebootSystem(){
        String[] commandList = new String[]{
                "reboot",
        };
        return ShellUtils.exeCmdByRoot(commandList);
    }


    /**
     * 执行shell指令
     * @param strings 指令集
     * @return 指令集是否执行成功
     */
    public static boolean exeCmdByRoot(String... strings) {
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            for (String s : strings) {
                outputStream.writeBytes(s + "\n");
                outputStream.flush();
            }
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
