package moe.leer.rafttools;

import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author: LeeReindeer
 * Time: 2020/2/6.
 */
public class Board extends JFrame {


  public static final byte CREATIVE_MODE = 2;
  public static final byte PEACEFUL_MODE = 5;
  public static final byte EASY_MODE = 3;
  public static final byte NORMAL_MODE = 0;
  public static final byte HARD_MODE = 1;

  public static final byte[] PATTERN = new byte[]{71, 97, 109, 101, 77, 111, 100, 101, 1, 0, 0, 0, 7, 118, 97, 108, 117, 101, 95, 95, 0, 8, 2, 0, 0, 0};

  private static byte[] buffer;
  private static int modeByteIndex;

  public Board() throws HeadlessException {

    try {
      UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialOrientalFontsTheme()));
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }


    this.setTitle("Raft存档修改器");
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().setLayout(new BorderLayout());
    this.setSize(650, 400);

    JComboBox<GameMode> comboBox = new JComboBox<>();
    comboBox.addItem(GameMode.CREATIVE_MODE);
    comboBox.addItem(GameMode.PEACEFUL_MODE);
    comboBox.addItem(GameMode.EASY_MODE);
    comboBox.addItem(GameMode.NORMAL_MODE);
    comboBox.addItem(GameMode.HARD_MODE);

    JPanel readPanel = new JPanel(new BorderLayout());
    JPanel modifyPanel = new JPanel(new BorderLayout());

    JLabel pathLabel = new JLabel("存档路径：");
    JTextField filePathField = new JFormattedTextField();
    JLabel modeLabel = new JLabel("当前模式：");
    JButton readButton = new JButton("读取");
    // on hover, button will change to a light gray
    MaterialUIMovement.add(readButton, MaterialColors.GRAY_100);
    readButton.addActionListener(e -> {
      String path = filePathField.getText().replace("\\", "\\\\");
      System.out.println(path);
      String mode = readMode(path);
      modeLabel.setText("当前模式：" + mode);

    });
    readPanel.add(pathLabel, BorderLayout.WEST);
    readPanel.add(filePathField, BorderLayout.CENTER);
    readPanel.add(readButton, BorderLayout.EAST);
    readPanel.add(modeLabel, BorderLayout.SOUTH);

    JButton modifyButton = new JButton("修改");
    MaterialUIMovement.add(modifyButton, MaterialColors.GRAY_100);
    modifyButton.addActionListener(e -> {
      String path = filePathField.getText().replace("\\", "\\\\");
      GameMode gameMode = (GameMode) (comboBox.getSelectedItem());
      if (modifyMode(gameMode, path)) {
        String mode = readMode(path);
        modeLabel.setText("当前模式：" + mode);
      } else {
        modeLabel.setText("当前模式：" + "无效的路径或存档格式");
      }

    });
    modifyPanel.add(comboBox, BorderLayout.CENTER);
    modifyPanel.add(modifyButton, BorderLayout.EAST);
    modifyPanel.add(new JLabel("选择模式："), BorderLayout.WEST);

    this.getContentPane().add(readPanel, BorderLayout.NORTH);
    this.getContentPane().add(new JLabel("<html><h2>操作说明</h2>1.Raft 游戏中选择载入游戏，点击OPEN SAVE FOLDER PATH<br/>2.在弹出的文件管理器中进入World文件夹，再进入要修改的存档文件夹<br/>3.复制其路径到本程序的存档路径框框中<br/>4.例如：C:\\Users\\&lt;用户名&gt;\\AppData\\LocalLow\\Redbeet Interactive\\Raft\\User\\User_xxxxxxxxx\\World\\存档名</html>", SwingConstants.CENTER), BorderLayout.CENTER);
    this.getContentPane().add(modifyPanel, BorderLayout.SOUTH);

    this.setVisible(true);
  }

  public static void main(String[] args) {
    Board board = new Board();
  }

  private String[] getGameArchivePaths(String path) {
    if (path.endsWith("\\")) {
      path = path.substring(0, path.length() - 2);
      System.out.println(path);
    }
    path = path.replace("\\\\", "\\");
    path = path.replace("\\", "\\\\");
    String archiveName = path.substring(path.lastIndexOf("\\") + 1);
    File dir = new File(path);
    String[] backups = dir.list();
    if (backups != null && backups.length > 0) {
      String[] paths = new String[backups.length];
      for (int i = 0; i < backups.length; i++) {
        paths[i] = path + "\\" + backups[i] + "\\" + archiveName + ".rgd";
        File file = new File(paths[i]);
        if (!file.exists()) {
          return null;
        }
      }
      return paths;
    } else {
      return null;
    }
  }


  private String readMode(String path) {
    try {
      if (!path.endsWith(".rgd")) {
        String[] paths = getGameArchivePaths(path);
        if (paths == null || paths.length == 0) {
          return "无效的路径";
        }
        path = paths[paths.length - 1];
        System.out.println("Read latest archive: " + path);
      }
      buffer = Files.readAllBytes(Paths.get(path));
      int i = KMPMatch.indexOf(buffer, PATTERN);
      if (i == -1) {
        return "无效的格式";
      }
      System.out.println(buffer[i + PATTERN.length]);
      modeByteIndex = i + PATTERN.length;
      GameMode gameMode = GameMode.valueOfByte(buffer[modeByteIndex]);
      if (gameMode != null) {
        return gameMode.name;
      } else {
        return "无效的格式";
      }
    } catch (IOException e) {
      e.printStackTrace();
      return e.getMessage();
    }
  }

  private boolean modifyMode(GameMode mode, String path) {
    try {
      if (!path.endsWith(".rgd")) {
        String[] paths = getGameArchivePaths(path);
        if (paths == null || paths.length == 0) {
          return false;
        }
        path = paths[paths.length - 1];
        System.out.println("Write latest archive: " + path);
      }
      if (buffer == null) {
        buffer = Files.readAllBytes(Paths.get(path));
      }
      buffer[modeByteIndex] = mode.value;
      System.out.println(GameMode.valueOfByte(buffer[modeByteIndex]).name);
      Path path1 = Files.write(Paths.get(path), buffer);
      System.out.println(path1.getFileName());
      System.out.println("Write succeed");
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
