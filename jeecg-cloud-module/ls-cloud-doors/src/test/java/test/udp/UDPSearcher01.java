package test.udp;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * UDP 搜索者，用于搜索服务支持方
 */
public class UDPSearcher01 {
    public static void main(String[] args){
        List<String> list = Lists.newArrayList();
        list.add("49_55_50_46_49_56_46_49_57_51_46_49_51_53");
        list.add("50_53_53_46_50_53_53_46_50_52_56_46_48");
        list.add("49_55_50_46_49_56_46_49_57_50_46_49");
        list.add("49_55_50_46_49_56_46_49_57_50_46_49");
        list.add("49_54_58_48_65_58_48_68_58_50_51_58_49_52_58_66_55");
        list.add("2_19");
        list.add("73_80_67");
        list.add("80");
        list.add("2_42");
        list.add("80");
        list.add("44");
        list.add("73_80_67_65_77_69_82_65");
        list.add("70_71_50_95_49_54_78_82_95_66_86_67_53_76_51_65_48_84_49_81_48_95_86_49_46_48_46_48_46_50_49_48_55_49_52_95_82_49");
        list.add("49_48_48_48_48_48_48_48_48_49_50_49_50");
        list.add("97_100_109_105_110");
        list.add("68_86_51");
        list.add("117_84");
        list.add("46");
        list.add("41_102");
        list.add("41_102");
        list.add("46");
        list.add("41_102");
        list.add("97");
        list.add("94");
        list.add("126");
        list.add("46");
        list.add("41_102");
        list.add("46");
        list.add("41_102");
        list.add("46");
        list.add("41_102");
        for (int j = 0; j < list.size(); j++) {
            byte [] bytes = new byte[1024];
            String byteStr = list.get(j);
            String [] byteList = byteStr.split("_");
            int i = 0;
            for (String s : byteList) {
                bytes[i] = Byte.parseByte(s);
                i++;
            }
            System.out.println("第" + j + "--->" + new String(bytes));
        }
    }
}


