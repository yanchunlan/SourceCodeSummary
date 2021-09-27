package thread;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:  yanchunlan
 * date:  2021/09/22 17:45
 * desc:
 */
public class Thread_Test {

  public static void main(String[] args) throws IOException {
    java.io.File file = new java.io.File("asmlib/src/main/java/thread");
    if (!file.isDirectory()) {
      return;
    }
    Result result = new Result();
    List<String> list = new ArrayList<>();
    int count = 0;
    for (java.io.File f : file.listFiles()) {
      if (f.getName().startsWith("thread-")) {
        boolean isAfter = f.getName().contains("after");
        BufferedReader br = null;
        try {
          br = new BufferedReader(new FileReader(f));
          String line;
          while ((line = br.readLine()) != null) {
            int start = (line.indexOf("0 S ") > 0 ?
                line.indexOf("0 S ") : line.indexOf("0 R ")) + 4;
            line = line.substring(start, line.length());
            if (isAfter) {
              list.add(line);
            } else {
              list.remove(line);
            }
            ++count;
          }
          br.close();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            br.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (isAfter) {
          result.after_count = count;
        } else {
          result.before_count = count;
        }
        count = 0;
      }
    }
    HashMap<String, Integer> map = new HashMap<>();
    for (String item : list) {
      Integer value = map.get(item);
      if (value == null || value == -1) {
        if (item.startsWith("pool-") && item.endsWith("-thread-")) {
          Integer v = map.get("pool-xx-thread-");
          if (v == null || v == -1) {
            map.put("pool-xx-thread-", 1);
          } else {
            map.put("pool-xx-thread-", v + 1);
          }
        } else if (item.startsWith("Thread-")) {
          Integer v = map.get("Thread-xx");
          if (v == null || v == -1) {
            map.put("Thread-xx", 1);
          } else {
            map.put("Thread-xx", v + 1);
          }
        } else {
          map.put(item, 1);
        }
      } else {
        map.put(item, value + 1);
      }
    }
    List<Map.Entry<String, Integer>> resultList = new ArrayList<>(map.entrySet());
    Collections.sort(resultList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));


    result.increment = resultList;
    File fileOut = new File(file, "thread-increment.txt");
    BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(fileOut));
    fout.write(result.toString().getBytes());
    fout.close();
    fout.flush();
  }

  static class Result {
    int before_count;
    int after_count;
    List<Map.Entry<String, Integer>> increment;

    @Override
    public String toString() {
      return "{" +
          "\nbefore_count = " + before_count +
          ", \nafter_count = " + after_count +
          ", \nincrement=" + getAddString(increment) +
          '}';
    }

    private String getAddString(List<Map.Entry<String, Integer>> list) {
      StringBuilder sb = new StringBuilder();
      sb.append("[\n");
      for (Map.Entry<String, Integer> mapping : list) {
        sb.append(mapping.getKey()).append(" = ").append(mapping.getValue()).append(",\n");
      }
      sb.append("]");
      return sb.toString();
    }
  }

}
