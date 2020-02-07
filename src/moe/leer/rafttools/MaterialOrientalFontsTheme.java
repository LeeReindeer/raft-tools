package moe.leer.rafttools;

import mdlaf.themes.MaterialLiteTheme;

import java.awt.*;

/**
 * @author https://github.com/vincenzopalazzo
 */
public class MaterialOrientalFontsTheme extends MaterialLiteTheme {

  @Override
  protected void installFonts() {

        /*this.fontBold = new javax.swing.plaf.FontUIResource("Noto Sans", Font.BOLD,14);
        this.fontItalic = new javax.swing.plaf.FontUIResource("Noto Sans", Font.ITALIC,14);
        this.fontMedium = new javax.swing.plaf.FontUIResource("Noto Sans", Font.PLAIN,14);
        this.fontRegular = new javax.swing.plaf.FontUIResource("Noto Sans", Font.PLAIN,14);*/

    this.fontBold = new javax.swing.plaf.FontUIResource(Font.SANS_SERIF, Font.BOLD,12);
    this.fontItalic = new javax.swing.plaf.FontUIResource(Font.SANS_SERIF, Font.ITALIC,12);
    this.fontMedium = new javax.swing.plaf.FontUIResource(Font.SANS_SERIF, Font.PLAIN,12);
    this.fontRegular = new javax.swing.plaf.FontUIResource(Font.SANS_SERIF, Font.PLAIN,12);
  }
}