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

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author:  yanchunlan
 * date:  2021/09/22 17:45
 * desc:
 */
public class Fd_Test {

  public static void main(String[] args) throws IOException {
    Result result = new Result();
    String dir = "asmlib/src/main/java/thread";
    filterIncrement(dir, result);

    String outPath = "fd-increment.txt";
    File outFile = new File(dir, outPath);
    save(outFile, result.toString().getBytes());
    return;
  }

  @Nullable
  private static void filterIncrement(String dir, Result result) throws IOException {
    List<String> before = new ArrayList<>();
    List<String> after = new ArrayList<>();
    List<String> increment = new ArrayList<>();

    File file = new File(dir);
    if (!file.isDirectory()) {
      return;
    }
    for (File f : file.listFiles()) {
      if (f.getName().startsWith("fd-")) {
        boolean isAfter = f.getName().contains("after");
        BufferedReader br = null;
        try {
          br = new BufferedReader(new FileReader(f));
          String line;
          while ((line = br.readLine()) != null) {
            if (!line.contains("->")) {
              continue;
            }
            int start = line.indexOf("->") + 3;
            if (start >= line.length()) {
              continue;
            }
            line = line.substring(start, line.length());
            if (isAfter) {
              after.add(line);
            } else {
              before.add(line);
              increment.remove(line);
            }
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
          increment.addAll(after);
        }
      }
    }

    // print after
    String outPathAfter = "fd-after-calute.txt";
    File outFileAfter = new File(dir, outPathAfter);
    String outListAfter = sortResult(after).toString().replaceAll(", ", ", \n");
    save(outFileAfter, outListAfter.getBytes());

    // print before
    String outPathBefore = "fd-before-calute.txt";
    File outFileBefore = new File(dir, outPathBefore);
    String outListBefore = sortResult(before).toString().replaceAll(", ", ", \n");
    save(outFileBefore, outListBefore.getBytes());

    result.before_count = before.size();
    result.after_count = after.size();
    result.increment = sortResult(increment);
  }

  private static void save(File file, byte[] bytes) throws IOException {
    BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(file));
    fout.write(bytes);
    fout.close();
    fout.flush();
  }

  @NotNull
  private static List<Map.Entry<String, Integer>> sortResult(List<String> list) {
    HashMap<String, Integer> map = new HashMap<>();
    for (String item : list) {
      if (item.startsWith("socket:[")) {
        Integer v = map.get("socket:[xxx]");
        if (v == null || v == -1) {
          map.put("socket:[xxx]", 1);
        } else {
          map.put("socket:[xxx]", v + 1);
        }
      } else {
        if (TextUtils.isEmpty(item) || item.contains("=") || item.equals(" ") || item.equals(", ")|| item.equals("")) {
          continue;
        }
        Integer value = map.get(item);
        if (value == null || value == -1) {
          map.put(item, 1);
        } else {
          map.put(item, value + 1);
        }
      }
    }
    List<Map.Entry<String, Integer>> resultList = new ArrayList<>(map.entrySet());
    Collections.sort(resultList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    return resultList;
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
          ", \nincrement_count=" + (after_count - before_count) +
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
