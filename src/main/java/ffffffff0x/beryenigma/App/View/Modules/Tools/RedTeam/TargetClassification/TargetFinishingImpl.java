package ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.TargetClassification;

import ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.TargetClassification.Engine.TargetFinishingBean;
import ffffffff0x.beryenigma.Kit.Utils.FileUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: RyuZUSUNC
 * @create: 2021-05-06 10:01
 **/

public class TargetFinishingImpl {

    private static TargetFinishingBean resultBean = new TargetFinishingBean();

    public static TargetFinishingBean TargetClassification(Object source){

        StringBuilder IP = new StringBuilder();
        StringBuilder url = new StringBuilder();
        StringBuilder IPPort = new StringBuilder();

        int numIP = 0;
        int numurl = 0;
        int numIPPort = 0;

        HashSet<String> hashset = new HashSet(FileUtils.readLine(source));
        for (String line:hashset) {
            if(isIP(line)){
                IP.append(line).append(" ");
                numIP++;
            }else if(isIPPort(line)){
                IPPort.append(line).append(" ");
                numIPPort++;
            }else {
                url.append(line).append(" ");
                numurl++;
            }
        }

        resultBean.setAllIP(ipSort(IP.toString()));
        resultBean.setAllUrl(urlSort(url.toString()));
        resultBean.setAllIPPort(urlSort(IPPort.toString()));
        resultBean.setNumIP(numIP);
        resultBean.setNumUrl(numurl);
        resultBean.setNumIPPort(numIPPort);
        resultBean.setNumduplication(hashset.size());

        return resultBean;
    }

    /**
     * 判断是否是IP地址
     * @param ip
     * @return
     */
    private static boolean isIP(String ip){
        //ip地址范围：(1~255).(0~255).(0~255).(0~255)
        final String ipRegEx = "^([1-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))(\\.([0-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))){3}$";
        Pattern pattern = Pattern.compile(ipRegEx);
        Matcher matcher = pattern.matcher(ip);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是IP地址加端口号
     * @param ipport
     * @return
     */
    private static boolean isIPPort(String ipport){
        //ipport范围：(1~255).(0~255).(0~255).(0~255):(0-65535)
        final String domainRegEx = "^([1-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))(\\.([0-9]|([1-9][0-9])|(1[0-9][0-9])|(2[0-4][0-9])|(25[0-5]))){3}\\:\\d*$";
        Pattern pattern = Pattern.compile(domainRegEx);
        Matcher matcher = pattern.matcher(ipport);
        return matcher.matches();
    }

    /**
     * URL类型的排序
     * @param source
     * @return
     */
    private static String urlSort(String source){
        String[] sortStr = source.split(" ");
        Arrays.sort(sortStr);
        StringBuilder result = new StringBuilder();
        for (String s : sortStr) {
            result.append(s).append('\n');
        }
        return result.toString();
    }

    /**
     * IP类型的排序
     * @param source
     * @return
     */
    private static String ipSort(String source){
        // 为了让IP可以按字符串顺序比较，每一位需要0补充
        source = source.replaceAll("(\\d+)", "00$1");
//        System.out.println(source);

        // 然后每一个保留3位
        source = source.replaceAll("0*(\\d{3})", "$1");
//        System.out.println(source);

        // IP地址分割并排序
        String[] ips = source.split(" +");
        Set<String> set = new TreeSet<>();
        Collections.addAll(set, ips);
        StringBuilder result = new StringBuilder();
        for (String ip : set) {
            result.append(ip.replaceAll("0*(\\d+)", "$1")).append('\n');
        }
        return result.toString();
    }
}
