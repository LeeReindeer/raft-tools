package moe.leer.rafttools;

/**
 * Author: LeeReindeer
 * Time: 2020/2/6.
 */
public enum GameMode {

  CREATIVE_MODE("Creative(创意)", (byte)2),
  PEACEFUL_MODE("Peaceful(和平)", (byte)5),
  EASY_MODE("Easy(易)", (byte)3),
  NORMAL_MODE("Normal(普通)", (byte)0),
  HARD_MODE("Hard(困难)", (byte)1);

  public final byte value;
  public final String name;

  GameMode(String name, byte value){
    this.name = name;
    this.value = value;
  }

  public static GameMode valueOfByte(byte value) {
    for (GameMode mode : values()) {
      if (mode.value == value) return mode;
    }
    return null;
  }


  @Override
  public String toString() {
    return name;
  }
}
